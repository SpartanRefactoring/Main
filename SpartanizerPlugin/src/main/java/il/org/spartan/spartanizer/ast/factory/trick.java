package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** A number of utility functions common to all tippers.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-17 */
public enum trick {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static void addAllReplacing(final Collection<Statement> to, final Iterable<Statement> from, final Statement substitute, final Statement by1,
      final Iterable<Statement> by2) {
    for (final Statement ¢ : from)
      if (¢ != substitute)
        copy.into(¢, to);
      else {
        copy.into(by1, to);
        copy.into(by2, to);
      }
  }

  /** @param d JD
   * @param m JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addMethodToType(final AbstractTypeDeclaration d, final MethodDeclaration m, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(d, d.getBodyDeclarationsProperty()).insertLast(ASTNode.copySubtree(d.getAST(), m), g);
  }

  /** @param d JD
   * @param s JD
   * @param r rewriter
   * @param g edit group, usually null */
  public static void addStatement(final MethodDeclaration d, final ReturnStatement s, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(step.body(d), Block.STATEMENTS_PROPERTY).insertLast(s, g);
  }

  public static Expression eliminateLiteral(final InfixExpression x, final boolean b) {
    final List<Expression> $ = extract.allOperands(x);
    trick.removeAll(b, $);
    switch ($.size()) {
      case 1:
        return copy.of(first($));
      case 0:
        return x.getAST().newBooleanLiteral(b);
      default:
        return subject.operands($).to(x.getOperator());
    }
  }

  public static ListRewrite insertAfter(final Statement where, final List<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(where.getParent(), Block.STATEMENTS_PROPERTY);
    for (int ¢ = what.size() - 1;; $.insertAfter(what.get(¢--), where, g))
      if (¢ < 0)
        return $;
  }

  public static ListRewrite insertBefore(final Statement where, final Iterable<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(parent(where), Block.STATEMENTS_PROPERTY);
    what.forEach(λ -> $.insertBefore(λ, where, g));
    return $;
  }

  static int positivePrefixLength(final IfStatement $) {
    return metrics.length($.getExpression(), then($));
  }

  /** As {@link elze(ConditionalExpression)} but returns the last else statement
   * in "if - else if - ... - else" statement
   * @param ¢ JD
   * @return last nested else statement */
  public static Statement recursiveElse(final IfStatement ¢) {
    for (Statement $ = ¢.getElseStatement();; $ = ((IfStatement) $).getElseStatement())
      if (!($ instanceof IfStatement))
        return $;
  }

  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * <p>
   * @param ¢ JD
   * @param xs JD */
  public static void removeAll(final boolean ¢, final List<Expression> xs) {
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      final Expression x = wizard.find(¢, xs);
      if (x == null)
        return;
      xs.remove(x);
    }
  }

  public static void rename(final SimpleName oldName, final SimpleName newName, final ASTNode where, final ASTRewrite r, final TextEditGroup g) {
    new Inliner(oldName, r, g).byValue(newName).inlineInto(collect.usesOf(oldName).in(where).toArray(new SimpleName[0]));
  }

  public static ASTRewrite replaceTwoStatements(final ASTRewrite r, final Statement what, final Statement by, final TextEditGroup g) {
    final Block parent = az.block(what.getParent());
    final List<Statement> siblings = extract.statements(parent);
    final int i = siblings.indexOf(what);
    siblings.remove(i);
    siblings.remove(i);
    siblings.add(i, by);
    final Block $ = parent.getAST().newBlock();
    copy.into(siblings, statements($));
    r.replace(parent, $, g);
    return r;
  }

  public static boolean shoudlInvert(final IfStatement s) {
    final int $ = wizard.sequencerRank(hop.lastStatement(then(s))), rankElse = wizard.sequencerRank(hop.lastStatement(elze(s)));
    return rankElse > $ || $ == rankElse && !trick.thenIsShorter(s);
  }

  public static boolean thenIsShorter(final IfStatement s) {
    final Statement then = then(s), elze = elze(s);
    if (elze == null)
      return true;
    final int s1 = count.lines(then), s2 = count.lines(elze);
    if (s1 < s2)
      return true;
    if (s1 > s2)
      return false;
    assert s1 == s2;
    final int n2 = extract.statements(elze).size(), n1 = extract.statements(then).size();
    if (n1 < n2)
      return true;
    if (n1 > n2)
      return false;
    assert n1 == n2;
    final IfStatement $ = make.invert(s);
    return positivePrefixLength($) >= positivePrefixLength(make.invert($));
  }
}
