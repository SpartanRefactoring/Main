package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** <pre>
 * a ? b : c
 * </pre>
 *
 * is the same as
 *
 * <pre>
 * (a && b) || (!a && c)
 * </pre>
 *
 * if b is false than:
 *
 * <pre>
 * (a && false) || (!a && c) == (!a && c)
 * </pre>
 *
 * if b is true than:
 *
 * <pre>
 * (a && true) || (!a && c) == a || (!a && c) == a || c
 * </pre>
 *
 * if c is false than:
 *
 * <pre>
 * (a && b) || (!a && false) == (!a && c)
 * </pre>
 *
 * if c is true than
 *
 * <pre>
 * (a && b) || (!a && true) == (a && b) || (!a) == !a || b
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
public final class TernaryBooleanLiteral extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  @Override Expression replacement(final ConditionalExpression e) {
    return simplifyTernary(e);
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return isTernaryOfBooleanLitreral(e);
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
   * (a && b) || (!a && c)
   * </pre>
   * <ol>
   * <li>if b is false then:
   *
   * <pre>
   * (a && false) || (!a && c) == !a && c
   * </pre>
   *
   * <li>if b is true then:
   *
   * <pre>
   * (a && true) || (!a && c) == a || (!a && c) == a || c
   * </pre>
   *
   * <li>if c is false then:
   *
   * <pre>
   * (a && b) || (!a && false) == a && b
   * </pre>
   *
   * <li>if c is true then
   *
   * <pre>
   * (a && b) || (!a && true) == !a || b
   * </pre>
   * </ol>
  */
  private static Expression simplifyTernary(final ConditionalExpression e) {
    return simplifyTernary(core(e.getThenExpression()), core(e.getElseExpression()), duplicate(e.getExpression()));
  }
  private static boolean isTernaryOfBooleanLitreral(final ConditionalExpression e) {
    return e != null && Have.booleanLiteral(core(e.getThenExpression()), core(e.getElseExpression()));
  }
  private static Expression simplifyTernary(final Expression then, final Expression elze, final Expression main) {
    final boolean takeThen = !Is.booleanLiteral(then);
    final Expression other = takeThen ? then : elze;
    final boolean literal = asBooleanLiteral(takeThen ? elze : then).booleanValue();
    return Subject.pair(literal != takeThen ? main : logicalNot(main), other).to(literal ? CONDITIONAL_OR : CONDITIONAL_AND);
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Convert conditional expression into logical expression";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.IF_TO_TERNARY;
  }
}
