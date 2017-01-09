package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** <pre>
 * a ? b : c
 * </pre>
 *
 * is the same as
 *
 * <pre>
 * (a &amp;&amp; b) || (!a &amp;&amp; c)
 * </pre>
 *
 * if b is false than:
 *
 * <pre>
 * (a &amp;&amp; false) || (!a &amp;&amp; c) == (!a &amp;&amp; c)
 * </pre>
 *
 * if b is true than:
 *
 * <pre>
 * (a &amp;&amp; true) || (!a &amp;&amp; c) == a || (!a &amp;&amp; c) == a || c
 * </pre>
 *
 * if c is false than:
 *
 * <pre>
 * (a &amp;&amp; b) || (!a &amp;&amp; false) == (!a &amp;&amp; c)
 * </pre>
 *
 * if c is true than
 *
 * <pre>
 * (a &amp;&amp; b) || (!a &amp;&amp; true) == (a &amp;&amp; b) || (!a) == !a || b
 * </pre>
 *
 * keywords
 *
 * <pre>
 * <b>this</b>
 * </pre>
 *
 * or
 *
 * <pre>
 * <b>null</b>
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class TernaryBooleanLiteral extends ReplaceCurrentNode<ConditionalExpression> implements TipperCategory.InVain {
  private static boolean isTernaryOfBooleanLitreral(final ConditionalExpression ¢) {
    return ¢ != null && have.booleanLiteral(core(¢.getThenExpression()), core(¢.getElseExpression()));
  }

  /** Consider an expression
   *
   * <pre>
   * a ? b : c
   * </pre>
   *
   * ; in a sense it is the same as
   *
   * <pre>
   * (a &amp;&amp; b) || (!a &amp;&amp; c)
   * </pre>
   * <ol>
   * <li>if b is false then:
   *
   * <pre>
   * (a &amp;&amp; false) || (!a &amp;&amp; c) == !a &amp;&amp; c
   * </pre>
   *
   * <li>if b is true then:
   *
   * <pre>
   * (a &amp;&amp; true) || (!a &amp;&amp; c) == a || (!a &amp;&amp; c) == a || c
   * </pre>
   *
   * <li>if c is false then:
   *
   * <pre>
   * (a &amp;&amp; b) || (!a &amp;&amp; false) == a &amp;&amp; b
   * </pre>
   *
   * <li>if c is true then
   *
   * <pre>
   * (a &amp;&amp; b) || (!a &amp;&amp; true) == !a || b
   * </pre>
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
