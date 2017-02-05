package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;



// TODO Ori Roth: choose more suitable category
// TODO Ori Roth: add tests for tipper
/** Extract method suffix into new method according to predefined heuristic.
 * @author Ori Roth
 * @since 2016 */
public class ExtractMethodSuffix extends ListReplaceCurrentNode<MethodDeclaration>//
    implements TipperCategory.Modular {
  // TODO Ori Roth: get more suitable names for constants
  private static final int MINIMAL_STATEMENTS_COUNT = 6;

  @Override  public String description( final MethodDeclaration ¢) {
    return "Split " + ¢.getName() + " into two logical parts";
  }

  @Override  public List<ASTNode> go( final ASTRewrite r,  final MethodDeclaration d,
      @SuppressWarnings("unused") final TextEditGroup __) {
    if (!isValid(d))
      return null;
    final MethodVariablesScanner $ = new MethodVariablesScanner(d);
    for (final Statement ¢ : $.statements()) {
      $.update();
      if ($.isOptionalForkPoint())
        return splitMethod(r, d, $.usedVariables(), ¢, sameParameters(d, $.usedVariables()));
    }
    return null;
  }

  private static boolean isValid( final MethodDeclaration ¢) {
    return !¢.isConstructor() && ¢.getBody() != null && statements(¢.getBody()).size() >= MINIMAL_STATEMENTS_COUNT;
  }

  /** @param d JD
   * @param ds variables list
   * @return <code><b>true</b></code> <em>iff</em> the method and the list
   *         contains same variables, in matters of type and quantity
   *         [[SuppressWarningsSpartan]] */
  private static boolean sameParameters( final MethodDeclaration d,  final List<VariableDeclaration> ds) {
    if (d.parameters().size() != ds.size())
      return false;
    final List<String> ts = ds.stream().map(
        ¢ -> (iz.singleVariableDeclaration(¢) ? az.singleVariableDeclaration(¢).getType() : az.variableDeclrationStatement(parent(¢)).getType()) + "")
        .collect(Collectors.toList());
    // NANO: to rest of function
    for (final SingleVariableDeclaration ¢ : parameters(d))
      if (!ts.contains(¢.getType() + ""))
        return false;
    return true;
  }

   private static List<ASTNode> splitMethod( final ASTRewrite r,  final MethodDeclaration d,
       final List<VariableDeclaration> ds, final Statement forkPoint, final boolean equalParams) {
    ds.sort(new NaturalVariablesOrder(d));
    final List<ASTNode> $ = new ArrayList<>();
    final MethodDeclaration d1 = copy.of(d);
    fixStatements(d, d1, r);
    statements(d1).subList(statements(d).indexOf(forkPoint) + 1, statements(d).size()).clear();
    final MethodInvocation i = d.getAST().newMethodInvocation();
    i.setName(copy.of(d.getName()));
    fixName(i, equalParams);
    ds.forEach(λ -> step.arguments(i).add(copy.of(name(λ))));
    if (d.getReturnType2().isPrimitiveType() && "void".equals(d.getReturnType2() + ""))
      statements(d1).add(d.getAST().newExpressionStatement(i));
    else {
      final ReturnStatement s = d.getAST().newReturnStatement();
      s.setExpression(i);
      statements(d1).add(s);
    }
    $.add(d1);
    final MethodDeclaration d2 = copy.of(d);
    fixStatements(d, d2, r);
    statements(d2).subList(0, statements(d).indexOf(forkPoint) + 1).clear();
    fixName(d2, equalParams);
    fixParameters(d, d2, ds);
    fixJavadoc(d2, ds);
    $.add(d2);
    return $;
  }

  private static void fixStatements(final MethodDeclaration d, final MethodDeclaration dx,  final ASTRewrite r) {
    statements(body(dx)).clear();
    statements(body(d)).forEach(λ -> statements(dx).add(az.statement(r.createCopyTarget(λ))));
  }

  private static void fixName( final MethodDeclaration d2, final boolean equalParams) {
    if (equalParams)
      d2.setName(d2.getAST().newSimpleName(fixName(d2.getName() + "")));
  }

  private static void fixName( final MethodInvocation i, final boolean equalParams) {
    if (equalParams)
      i.setName(i.getAST().newSimpleName(fixName(i.getName() + "")));
  }

   private static String fixName( final String ¢) {
    return !Character.isDigit(¢.charAt(¢.length() - 1)) ? ¢ + "2" : ¢.replaceAll(".$", ¢.charAt(¢.length() - 1) - '0' + 1 + "");
  }

  private static void fixParameters( final MethodDeclaration d,  final MethodDeclaration d2,
       final List<VariableDeclaration> ds) {
    d2.parameters().clear();
    for (final VariableDeclaration v : ds)
      if (v instanceof SingleVariableDeclaration)
        parameters(d2).add(copy.of((SingleVariableDeclaration) v));
      else {
        final SingleVariableDeclaration sv = d.getAST().newSingleVariableDeclaration();
        final VariableDeclarationStatement p = az.variableDeclrationStatement(v.getParent());
        sv.setName(copy.of(v.getName()));
        sv.setType(copy.of(p.getType()));
        extendedModifiers(p).forEach(λ -> extendedModifiers(sv).add((IExtendedModifier) copy.of((ASTNode) λ)));
        parameters(d2).add(sv);
      }
  }

  private static void fixJavadoc( final MethodDeclaration d,  final List<VariableDeclaration> ds) {
    final Javadoc j = d.getJavadoc();
    if (j == null)
      return;
    final List<TagElement> ts = step.tags(j);
    final List<String> ns = ds.stream().map(λ -> λ.getName() + "").collect(Collectors.toList());
    boolean hasParamTags = false;
    int tagPosition = -1;
    final List<TagElement> xs = new ArrayList<>();
    for (final TagElement ¢ : ts)
      if (TagElement.TAG_PARAM.equals(¢.getTagName()) && ¢.fragments().size() == 1 && first(fragments(¢)) instanceof SimpleName) {
        hasParamTags = true;
        if (tagPosition < 0)
          tagPosition = ts.indexOf(¢);
        if (!ns.contains(first(fragments(¢))))
          xs.add(¢);
        else
          ns.remove(first(fragments(¢)));
      }
    if (!hasParamTags)
      return;
    ts.removeAll(xs);
    for (final String s : ns) {
      final TagElement e = j.getAST().newTagElement();
      e.setTagName(TagElement.TAG_PARAM);
      fragments(e).add(j.getAST().newSimpleName(s));
      ts.add(tagPosition, e);
    }
  }

  @Override public ChildListPropertyDescriptor listDescriptor(@SuppressWarnings("unused") final MethodDeclaration __) {
    return TypeDeclaration.BODY_DECLARATIONS_PROPERTY;
  }

  public static class MethodVariablesScanner extends MethodScanner {
    // TODO Ori Roth: get more suitable names for constants
    // 1.0 means all statements but the last.
    private static final double MAXIMAL_STATEMENTS_BEFORE_FORK_DIVIDER = 1.0;// 2.0/3.0;
     final Map<VariableDeclaration, List<Statement>> uses;
     final List<VariableDeclaration> active;
     final List<VariableDeclaration> inactive;
    int variablesTerminated;

    public MethodVariablesScanner( final MethodDeclaration method) {
      super(method);
      uses = new HashMap<>();
      active = new ArrayList<>();
      inactive = new ArrayList<>();
      variablesTerminated = 0;
      for (final SingleVariableDeclaration ¢ : parameters(method)) {
        setUsesMapping(¢, 0);
        if (uses.containsKey(¢))
          active.add(¢);
        else
          ++variablesTerminated;
      }
    }

    @Override  public List<Statement> availableStatements() {
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

    @SuppressWarnings("boxing") private void setUsesMapping( final VariableDeclaration d, final int starting) {
      range.from(starting).to(statements.size()).forEach(λ -> setUsesMapping(d, statements.get(λ)));
    }

    private void setUsesMapping( final VariableDeclaration d, final Statement s) {
      if (collect.usesOf(d.getName()).in(s).isEmpty())
        return;
      uses.putIfAbsent(d, new ArrayList<>());
      uses.get(d).add(s);
    }
  }

  static class NaturalVariablesOrder implements Comparator<VariableDeclaration> {
     final List<SingleVariableDeclaration> ps;
     final List<Statement> ss;

    NaturalVariablesOrder( final MethodDeclaration method) {
      assert method != null;
      ps = parameters(method);
      ss = body(method) != null ? statements(method) : new LinkedList<>();
    }

    @Override public int compare( final VariableDeclaration d1,  final VariableDeclaration d2) {
      return ps.contains(d1) ? !ps.contains(d2) ? 1 : ps.indexOf(d1) - ps.indexOf(d2)
          : ps.contains(d2) ? -1
              : d1.getParent() != d2.getParent() ? ss.indexOf(d1.getParent()) - ss.indexOf(d2.getParent())
                  : ((VariableDeclarationStatement) d1.getParent()).fragments().indexOf(d1)
                      - ((VariableDeclarationStatement) d2.getParent()).fragments().indexOf(d2);
    }
  }
}