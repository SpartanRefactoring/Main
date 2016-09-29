package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

// TODO Ori: choose more suitable category
// TODO Ori: add tests
/** Extract method suffix into new method according to predefined heuristic.
 * @author Ori Roth
 * @since 2016 */
public class ExtractMethodSuffix extends MultipleReplaceCurrentNode<MethodDeclaration> implements TipperCategory.EarlyReturn {
  // TODO Ori: get more suitable names for constants
  private static final int MINIMAL_STATEMENTS_COUNT = 6;
  private static final double MAXIMAL_STATEMENTS_BEFORE_FORK_DIVIDER = 2.0/3.0;

  @Override public String description(final MethodDeclaration d) {
    return "Split " + d.getName() + " into two logical parts";
  }

  @SuppressWarnings("unchecked") @Override public ASTRewrite go(final ASTRewrite r, final MethodDeclaration d,
      @SuppressWarnings("unused") final TextEditGroup __, final List<ASTNode> bss, final List<ASTNode> crs) {
    if (!isValid(d))
      return null;
    bss.add(d);
    final List<Statement> ss = d.getBody().statements();
    final Map<VariableDeclaration, List<Statement>> uses = new HashMap<>();
    final List<SingleVariableDeclaration> ps = d.parameters();
    for (final SingleVariableDeclaration v : ps)
      setUsesMapping(uses, v, ss, 0);
    for (final Statement s : optionalForkPoints(d)) {
      clearUsesMapping(uses, s);
      if (validForkPoint(uses, ps)) {
        updateUsesMapping(uses, ss, ss.indexOf(s));
        return go(r, d, uses, s, crs, sameParameters(d, uses.keySet()));
      }
      updateUsesMapping(uses, ss, ss.indexOf(s));
    }
    return null;
  }

  private static boolean isValid(final MethodDeclaration d) {
    return !d.isConstructor() && d.getBody() != null && d.getBody().statements().size() >= MINIMAL_STATEMENTS_COUNT;
  }
  
  private static void setUsesMapping(final Map<VariableDeclaration, List<Statement>> m, final VariableDeclaration d, final Statement s) {
    if (!Collect.usesOf(d.getName()).in(s).isEmpty()) {
      if (!m.containsKey(d))
        m.put(d, new LinkedList<>());
      m.get(d).add(s);
    }
  }
  
  private static void setUsesMapping(final Map<VariableDeclaration, List<Statement>> m, final VariableDeclaration d, final List<Statement> ss, final int starting) {
    for (int i = starting ; i < ss.size() ; ++i)
      setUsesMapping(m, d, ss.get(i));
  }
  
  private static List<Statement> optionalForkPoints(final MethodDeclaration d) {
    @SuppressWarnings("unchecked") List<Statement> ss = d.getBody().statements();
    return ss.subList(0, Math.min((int) (MAXIMAL_STATEMENTS_BEFORE_FORK_DIVIDER * ss.size()) + 1, ss.size()));
  }
  
  private static void clearUsesMapping(final Map<VariableDeclaration, List<Statement>> uses, final Statement s) {
    final List<VariableDeclaration> vs = new LinkedList<>();
    vs.addAll(uses.keySet());
    for (final VariableDeclaration d : vs) {
      uses.get(d).remove(s);
      if (uses.get(d).isEmpty())
        uses.remove(d);
    }
  }
  
  private static boolean validForkPoint(final Map<VariableDeclaration, List<Statement>> uses,
      @SuppressWarnings("unused") final List<SingleVariableDeclaration> ps) {
    // for (final SingleVariableDeclaration p : ps)
    // if (uses.containsKey(p))
    // return false;
    // return true;
    return uses.isEmpty();
  }
  
  @SuppressWarnings("unchecked") private static void updateUsesMapping(final Map<VariableDeclaration, List<Statement>> m, final List<Statement> ss, final int i) {
    if (!(ss.get(i) instanceof VariableDeclarationStatement))
      return;
    for (final VariableDeclarationFragment f : (List<VariableDeclarationFragment>) ((VariableDeclarationStatement) ss.get(i)).fragments())
      setUsesMapping(m, f, ss, i + 1);
  }
  
  @SuppressWarnings("unchecked") private static boolean sameParameters(MethodDeclaration d, Set<VariableDeclaration> s) {
    if (d.parameters().size() != s.size())
      return false;
    List<String> ts = new LinkedList<>();
    for (VariableDeclaration t : s)
      ts.add((t instanceof SingleVariableDeclaration ? ((SingleVariableDeclaration) t).getType()
          : az.variableDeclrationStatement(t.getParent()).getType()) + "");
    for (SingleVariableDeclaration v : (List<SingleVariableDeclaration>) d.parameters())
      if (!ts.contains(v.getType() + ""))
        return false;
    return true;
  }
  
  @SuppressWarnings("unchecked") private static ASTRewrite go(final ASTRewrite r, final MethodDeclaration d,
      final Map<VariableDeclaration, List<Statement>> m, final Statement forkPoint, final List<ASTNode> crs, boolean equalParams) {
    final MethodDeclaration d1 = duplicate.of(d);
    d1.getBody().statements().subList(d.getBody().statements().indexOf(forkPoint) + 1, d.getBody().statements().size()).clear();
    final MethodInvocation i = d.getAST().newMethodInvocation();
    i.setName(duplicate.of(d.getName()));
    for (final VariableDeclaration v : m.keySet())
      i.arguments().add(duplicate.of(v.getName()));
    if (d.getReturnType2().isPrimitiveType() && "void".equals(d.getReturnType2() + ""))
      d1.getBody().statements().add(d.getAST().newExpressionStatement(i));
    else {
      final ReturnStatement s = d.getAST().newReturnStatement();
      s.setExpression(i);
      d1.getBody().statements().add(s);
    }
    crs.add(d1);
    final MethodDeclaration d2 = duplicate.of(d);
    d2.getBody().statements().subList(0, d.getBody().statements().indexOf(forkPoint) + 1).clear();
    fixName(d2, equalParams);
    fixParameters(d, d2, m);
    fixJavadoc(d2, m);
    crs.add(d2);
    return r;
  }

  private static void fixName(MethodDeclaration d2, boolean equalParams) {
    if (equalParams)
      d2.setName(d2.getAST().newSimpleName(d2.getName() + "2"));
  }

  @SuppressWarnings("unchecked") private static void fixParameters(final MethodDeclaration d, final MethodDeclaration d2,
      final Map<VariableDeclaration, List<Statement>> m) {
    d2.parameters().clear();
    for (final VariableDeclaration v : m.keySet())
      if (v instanceof SingleVariableDeclaration)
        d2.parameters().add(duplicate.of(v));
      else {
        final SingleVariableDeclaration sv = d.getAST().newSingleVariableDeclaration();
        final VariableDeclarationStatement p = az.variableDeclrationStatement(v.getParent());
        sv.setName(duplicate.of(v.getName()));
        sv.setType(duplicate.of(p.getType()));
        for (IExtendedModifier md : (List<IExtendedModifier>) p.modifiers())
          sv.modifiers().add(duplicate.of((ASTNode) md));
        d2.parameters().add(sv);
      }
  }
  
  @SuppressWarnings("unchecked") private static void fixJavadoc(MethodDeclaration d, final Map<VariableDeclaration, List<Statement>> m) {
    final Javadoc j = d.getJavadoc();
    if (j == null)
      return;
    final List<TagElement> ts = j.tags();
    final List<String> ns = new LinkedList<>();
    for (final VariableDeclaration v : m.keySet())
      ns.add(v.getName() + "");
    boolean hasParamTags = false;
    final List<TagElement> xs = new LinkedList<>();
    for (final TagElement t : ts)
      if (TagElement.TAG_PARAM.equals(t.getTagName()) && t.fragments().size() == 1 && t.fragments().get(0) instanceof SimpleName) {
        hasParamTags = true;
        if (!ns.contains(t.fragments().get(0)))
          xs.add(t);
        else
          ns.remove(t.fragments().get(0));
      }
    if (!hasParamTags)
      return;
    ts.removeAll(xs);
    for (final String s : ns) {
      TagElement e = j.getAST().newTagElement();
      e.setTagName(TagElement.TAG_PARAM);
      e.fragments().add(j.getAST().newSimpleName(s));
      ts.add(e);
    }
  }
}
