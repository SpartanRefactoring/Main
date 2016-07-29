package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;
import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.asBooleanLiteral;
import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.logicalNot;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Have;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;

/**
 * <code>a ? b : c</code> is the same as <code>(a && b) || (!a && c)</code> if b
 * is false than: <code>(a && false) || (!a && c) == (!a && c)</code> if b is
 * true than: <code>(a && true) || (!a && c) == a || (!a && c) == a || c</code>
 * if c is false than: <code>(a && b) || (!a && false) == (!a && c)</code> if c
 * is true than
 * <code>(a && b) || (!a && true) == (a && b) || (!a) == !a || b</code> keywords
 * <code><b>this</b></code> or <code><b>null</b></code>.
 *
 * @author Yossi Gil
 * @since 2015-07-20
 */
public final class TernaryBooleanLiteral extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  @Override Expression replacement(final ConditionalExpression e) {
    return simplifyTernary(e);
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return isTernaryOfBooleanLitreral(e);
  }
  /**
   * Consider an expression <code>a ? b : c</code>; in a sense it is the same as
   * <code>(a && b) || (!a && c)</code>
   * <ol>
   * <li>if b is false then: <code>(a && false) || (!a && c) == !a && c</code>
   * <li>if b is true then:
   * <code>(a && true) || (!a && c) == a || (!a && c) == a || c</code>
   * <li>if c is false then: <code>(a && b) || (!a && false) == a && b</code>
   * <li>if c is true then <code>(a && b) || (!a && true) == !a || b</code>
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