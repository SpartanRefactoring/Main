package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** <code>
 * a ? b : c
 * </code>
 *
 * is the same as
 *
 * <code>
 * (a &amp;&amp; b) || (!a &amp;&amp; c)
 * </code>
 *
 * if b is false than:
 *
 * <code>
 * (a &amp;&amp; false) || (!a &amp;&amp; c) == (!a &amp;&amp; c)
 * </code>
 *
 * if b is true than:
 *
 * <code>
 * (a &amp;&amp; true) || (!a &amp;&amp; c) == a || (!a &amp;&amp; c) == a || c
 * </code>
 *
 * if c is false than:
 *
 * <code>
 * (a &amp;&amp; b) || (!a &amp;&amp; false) == (!a &amp;&amp; c)
 * </code>
 *
 * if c is true than
 *
 * <code>
 * (a &amp;&amp; b) || (!a &amp;&amp; true) == (a &amp;&amp; b) || (!a) == !a || b
 * </code>
 *
 * keywords
 *
 * <code>
 * <b>this</b>
 * </code>
 *
 * or
 *
 * <code>
 * <b>null</b>
 * </code>
 *
 * .
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class TernaryBooleanLiteral extends ReplaceCurrentNode<ConditionalExpression> //
    implements TipperCategory.NOP.onBooleans {
  private static boolean isTernaryOfBooleanLitreral(final ConditionalExpression ¢) {
    return ¢ != null && have.booleanLiteral(core(¢.getThenExpression()), core(¢.getElseExpression()));
  }

  /** Consider an expression
   *
   * <code>
   * a ? b : c
   * </code>
   *
   * in a sense it is the same as
   *
   * <code>
   * (a &amp;&amp; b) || (!a &amp;&amp; c)
   * </code>
   * <ol>
   * <li>if b is false then:
   *
   * <code>
   * (a &amp;&amp; false) || (!a &amp;&amp; c) == !a &amp;&amp; c
   * </code>
   *
   * <li>if b is true then:
   *
   * <code>
   * (a &amp;&amp; true) || (!a &amp;&amp; c) == a || (!a &amp;&amp; c) == a || c
   * </code>
   *
   * <li>if c is false then:
   *
   * <code>
   * (a &amp;&amp; b) || (!a &amp;&amp; false) == a &amp;&amp; b
   * </code>
   *
   * <li>if c is true then
   *
   * <code>
   * (a &amp;&amp; b) || (!a &amp;&amp; true) == !a || b
   * </code>
   * </ol>
  */
  private static Expression simplifyTernary(final ConditionalExpression ¢) {
    return simplifyTernary(core(¢.getThenExpression()), core(¢.getElseExpression()), copy.of(¢.getExpression()));
  }

  private static Expression simplifyTernary(final Expression then, final Expression elze, final Expression main) {
    final boolean $ = !iz.booleanLiteral(then);
    final Expression other = $ ? then : elze;
    final boolean literal = az.booleanLiteral($ ? elze : then).booleanValue();
    return subject.pair(literal != $ ? main : make.notOf(main), other).to(literal ? CONDITIONAL_OR : CONDITIONAL_AND);
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Convert conditional expression into logical expression";
  }

  @Override public boolean prerequisite(final ConditionalExpression ¢) {
    return isTernaryOfBooleanLitreral(¢);
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return simplifyTernary(¢);
  }
}
