package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

// TODO Roth: choose more suitable category
// TODO Roth: add tests for tipper
/** Extract method suffix into new method according to predefined heuristic.
 * @author Ori Roth
 * @since 2016 */
public class ExtractMethodSuffix extends ListReplaceCurrentNode<MethodDeclaration> implements TipperCategory.EarlyReturn {
  // TODO Roth: get more suitable names for constants
  private static final int MINIMAL_STATEMENTS_COUNT = 6;

  @Override public String description(final MethodDeclaration ¢) {
    return "Split " + ¢.getName() + " into two logical parts";
  }
  @Override public List<ASTNode> go(final ASTRewrite r, final MethodDeclaration d, @SuppressWarnings("unused") final TextEditGroup __) {
    if (!isValid(d))
      return null;
    final MethodVariablesScanner s = new MethodVariablesScanner(d);
    for (final Statement ¢ : s.statements()) {
      s.update();
      if (s.isOptionalForkPoint())
        return splitMethod(r, d, s.usedVariables(), ¢, sameParameters(d, s.usedVariables()));
    }
    return null;
  }
  private static boolean isValid(final MethodDeclaration ¢) {
    return !¢.isConstructor() && ¢.getBody() != null && ¢.getBody().statements().size() >= MINIMAL_STATEMENTS_COUNT;
  }
  /** @param d JD
   * @param ds variables list
   * @return <code><b>true</b></code> <em>iff</em> the method and the list
   *         contains same variables, in matters oftype and quantity
   *         [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unchecked") public static boolean sameParameters(final MethodDeclaration d, final List<VariableDeclaration> ds) {
    if (d.parameters().size() != ds.size())
      return false;
    final List<String> ts = new ArrayList<>();
    for (final VariableDeclaration ¢ : ds)
      ts.add((¢ instanceof SingleVariableDeclaration ? ((SingleVariableDeclaration) ¢).getType()
          : az.variableDeclrationStatement(¢.getParent()).getType()) + "");
    for (final SingleVariableDeclaration ¢ : (List<SingleVariableDeclaration>) d.parameters())
      if (!ts.contains(¢.getType() + ""))
        return false;
    return true;
  }
  @SuppressWarnings("unchecked") private static List<ASTNode> splitMethod(final ASTRewrite r, final MethodDeclaration d,
      final List<VariableDeclaration> ds, final Statement forkPoint, final boolean equalParams) {
    Collections.sort(ds, new NaturalVariablesOrder(d));
    final List<ASTNode> $ = new ArrayList<>();
    final MethodDeclaration d1 = duplicate.of(d);
    fixStatements(d, d1, r);
    d1.getBody().statements().subList(d.getBody().statements().indexOf(forkPoint) + 1, d.getBody().statements().size()).clear();
    final MethodInvocation i = d.getAST().newMethodInvocation();
    i.setName(duplicate.of(d.getName()));
    fixName(i, equalParams);
    for (final VariableDeclaration ¢ : ds)
      i.arguments().add(duplicate.of(¢.getName()));
    if (d.getReturnType2().isPrimitiveType() && "void".equals(d.getReturnType2() + ""))
      d1.getBody().statements().add(d.getAST().newExpressionStatement(i));
    else {
      final ReturnStatement s = d.getAST().newReturnStatement();
      s.setExpression(i);
      d1.getBody().statements().add(s);
    }
    $.add(d1);
    final MethodDeclaration d2 = duplicate.of(d);
    fixStatements(d, d2, r);
    d2.getBody().statements().subList(0, d.getBody().statements().indexOf(forkPoint) + 1).clear();
    fixName(d2, equalParams);
    fixParameters(d, d2, ds);
    fixJavadoc(d2, ds);
    $.add(d2);
    return $;
  }
  /** @param d
   * @param d1
   * @param r */
  @SuppressWarnings("unchecked") private static void fixStatements(final MethodDeclaration d, final MethodDeclaration dx, final ASTRewrite r) {
    dx.getBody().statements().clear();
    for (final Statement ¢ : (List<Statement>) d.getBody().statements())
      dx.getBody().statements().add(r.createCopyTarget(¢));
  }
  private static void fixName(final MethodDeclaration d2, final boolean equalParams) {
    if (equalParams)
      d2.setName(d2.getAST().newSimpleName(fixName(d2.getName() + "")));
  }
  private static void fixName(final MethodInvocation i, final boolean equalParams) {
    if (equalParams)
      i.setName(i.getAST().newSimpleName(fixName(i.getName() + "")));
  }
  private static String fixName(final String ¢) {
    return !Character.isDigit(¢.charAt(¢.length() - 1)) ? ¢ + "2" : ¢.replaceAll(".$", ¢.charAt(¢.length() - 1) - '0' + 1 + "");
  }
  @SuppressWarnings("unchecked") private static void fixParameters(final MethodDeclaration d, final MethodDeclaration d2,
      final List<VariableDeclaration> ds) {
    d2.parameters().clear();
    for (final VariableDeclaration v : ds)
      if (v instanceof SingleVariableDeclaration)
        d2.parameters().add(duplicate.of(v));
      else {
        final SingleVariableDeclaration sv = d.getAST().newSingleVariableDeclaration();
        final VariableDeclarationStatement p = az.variableDeclrationStatement(v.getParent());
        sv.setName(duplicate.of(v.getName()));
        sv.setType(duplicate.of(p.getType()));
        for (final IExtendedModifier md : (List<IExtendedModifier>) p.modifiers())
          sv.modifiers().add(duplicate.of((ASTNode) md));
        d2.parameters().add(sv);
      }
  }
  @SuppressWarnings("unchecked") private static void fixJavadoc(final MethodDeclaration d, final List<VariableDeclaration> ds) {
    final Javadoc j = d.getJavadoc();
    if (j == null)
      return;
    final List<TagElement> ts = j.tags();
    final List<String> ns = new ArrayList<>();
    for (final VariableDeclaration ¢ : ds)
      ns.add(¢.getName() + "");
    boolean hasParamTags = false;
    int tagPosition = -1;
    final List<TagElement> xs = new ArrayList<>();
    for (final TagElement ¢ : ts)
      if (TagElement.TAG_PARAM.equals(¢.getTagName()) && ¢.fragments().size() == 1 && ¢.fragments().get(0) instanceof SimpleName) {
        hasParamTags = true;
        if (tagPosition < 0)
          tagPosition = ts.indexOf(¢);
        if (!ns.contains(¢.fragments().get(0)))
          xs.add(¢);
        else
          ns.remove(¢.fragments().get(0));
      }
    if (!hasParamTags)
      return;
    ts.removeAll(xs);
    for (final String s : ns) {
      final TagElement e = j.getAST().newTagElement();
      e.setTagName(TagElement.TAG_PARAM);
      e.fragments().add(j.getAST().newSimpleName(s));
      ts.add(tagPosition, e);
    }
  }
  @Override public ChildListPropertyDescriptor listDescriptor(@SuppressWarnings("unused") final MethodDeclaration __) {
    return TypeDeclaration.BODY_DECLARATIONS_PROPERTY;
  }

  public static class MethodVariablesScanner extends MethodScanner {
    // TODO Roth: get more suitable names for constants
    // 1.0 means all statements but the last.
    private static final double MAXIMAL_STATEMENTS_BEFORE_FORK_DIVIDER = 1.0;// 2.0/3.0;
    protected final Map<VariableDeclaration, List<Statement>> uses;
    protected final List<VariableDeclaration> active;
    protected final List<VariableDeclaration> inactive;
    protected int variablesTerminated;

    @SuppressWarnings("unchecked") public MethodVariablesScanner(final MethodDeclaration method) {
      super(method);
      uses = new HashMap<>();
      active = new ArrayList<>();
      inactive = new ArrayList<>();
      variablesTerminated = 0;
      for (final SingleVariableDeclaration ¢ : (List<SingleVariableDeclaration>) method.parameters()) {
        setUsesMapping(¢, 0);
        if (uses.containsKey(¢))
          active.add(¢);
        else
          ++variablesTerminated;
      }
    }
    @Override public List<Statement> availableStatements() {
      return statements.subList(0, Math.min((int) (MAXIMAL_STATEMENTS_BEFORE_FORK_DIVIDER * statements.size()) + 1, statements.size() - 2));
    }
    @SuppressWarnings("unchecked") public void update() {
      final List<VariableDeclaration> vs = new ArrayList<>();
      vs.addAll(uses.keySet());
      for (final VariableDeclaration ¢ : vs) {
        if ((!(currentStatement instanceof ExpressionStatement) || !(((ExpressionStatement) currentStatement).getExpression() instanceof Assignment))
            && inactive.contains(¢) && uses.get(¢).contains(currentStatement)) {
          inactive.remove(¢);
          active.add(¢);
        }
        uses.get(¢).remove(currentStatement);
        if (uses.get(¢).isEmpty()) {
          uses.remove(¢);
          active.remove(¢);
          ++variablesTerminated;
        }
      }
      if (currentStatement instanceof VariableDeclarationStatement)
        for (final VariableDeclarationFragment ¢ : (List<VariableDeclarationFragment>) ((VariableDeclarationStatement) currentStatement)
            .fragments()) {
          setUsesMapping(¢, currentIndex + 1);
          if (uses.containsKey(¢))
            inactive.add(¢);
        }
    }
    public boolean isOptionalForkPoint() {
      return variablesTerminated > 0;// && active.isEmpty();
    }
    public List<VariableDeclaration> usedVariables() {
      final List<VariableDeclaration> $ = new ArrayList<>();
      $.addAll(uses.keySet());
      return $;
    }
    private void setUsesMapping(final VariableDeclaration d, final int starting) {
      for (int ¢ = starting; ¢ < statements.size(); ++¢)
        setUsesMapping(d, statements.get(¢));
    }
    private void setUsesMapping(final VariableDeclaration d, final Statement s) {
      if (Collect.usesOf(d.getName()).in(s).isEmpty())
        return;
      uses.putIfAbsent(d, new ArrayList<>());
      uses.get(d).add(s);
    }
  }

  static class NaturalVariablesOrder implements Comparator<VariableDeclaration> {
    final List<SingleVariableDeclaration> ps;
    final List<Statement> ss;

    @SuppressWarnings("unchecked") public NaturalVariablesOrder(final MethodDeclaration method) {
      assert method != null;
      ps = method.parameters();
      ss = method.getBody() == null ? Collections.EMPTY_LIST : method.getBody().statements();
    }
    @Override public int compare(final VariableDeclaration d1, final VariableDeclaration d2) {
      return ps.contains(d1) ? !ps.contains(d2) ? 1 : ps.indexOf(d1) - ps.indexOf(d2)
          : ps.contains(d2) ? -1
              : d1.getParent() != d2.getParent() ? ss.indexOf(d1.getParent()) - ss.indexOf(d2.getParent())
                  : ((VariableDeclarationStatement) d1.getParent()).fragments().indexOf(d1)
                      - ((VariableDeclarationStatement) d2.getParent()).fragments().indexOf(d2);
    }
  }
}