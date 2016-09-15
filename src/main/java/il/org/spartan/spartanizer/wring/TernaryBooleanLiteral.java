package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public final class TernaryBooleanLiteral extends ReplaceCurrentNode<ConditionalExpression> implements Kind.NOP {
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
    return simplifyTernary(core(¢.getThenExpression()), core(¢.getElseExpression()), duplicate.of(¢.getExpression()));
  }

  private static Expression simplifyTernary(final Expression then, final Expression elze, final Expression main) {
    final boolean takeThen = !iz.booleanLiteral(then);
    final Expression other = takeThen ? then : elze;
    final boolean literal = az.booleanLiteral(takeThen ? elze : then).booleanValue();
    return subject.pair(literal != takeThen ? main : make.notOf(main), other).to(literal ? CONDITIONAL_OR : CONDITIONAL_AND);
  }

  @Override public boolean demandsToSuggestButPerhapsCant(final ConditionalExpression ¢) {
    return isTernaryOfBooleanLitreral(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Convert conditional expression into logical expression";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return simplifyTernary(¢);
  }
}
