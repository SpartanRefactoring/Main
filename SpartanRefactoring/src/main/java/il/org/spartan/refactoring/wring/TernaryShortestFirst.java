package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.Funcs.asConditionalExpression;
import static il.org.spartan.refactoring.utils.Funcs.logicalNot;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;
import il.org.spartan.utils.LongestCommonSubsequence;

/**
 * A {@link Wring} to convert <code>a ? (f,g,h) : c(d,e)</code> into
 * <code>a ? c(d,e) : f(g,h)</code>
 *
 * @author Yossi Gil
 * @since 2015-08-14
 */
public final class TernaryShortestFirst extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  @Override ConditionalExpression replacement(final ConditionalExpression e) {
    final ConditionalExpression $ = Subject.pair(core(e.getElseExpression()), core(e.getThenExpression())).toCondition(logicalNot(e.getExpression()));
    final Expression then = $.getElseExpression();
    final Expression elze = $.getThenExpression();
    if (!Is.conditional(then) && Is.conditional(elze))
      return null;
    if (Is.conditional(then) && !Is.conditional(elze))
      return $;
    final ConditionalExpression parent = asConditionalExpression(e.getParent());
    if (parent != null && parent.getElseExpression() == e && compatibleCondition(parent.getExpression(), e.getExpression())) {
      final Expression alignTo = parent.getThenExpression();
      final double a1 = align(elze, alignTo);
      final double a2 = align(then, alignTo);
      if (Math.abs(a1 - a2) > 0.1)
        return a1 > a2 ? $ : null;
    }
    final Expression condition = logicalNot($.getExpression());
    return Wrings.length(condition, then) > Wrings.length(logicalNot(condition), elze) ? $ : null;
  }
  private static double align(final Expression e1, final Expression e2) {
    return new LongestCommonSubsequence(e1.toString(), e2.toString()).similarity();
  }
  private static boolean compatibleCondition(final Expression e1, final Expression e2) {
    return compatible(e1, e2) || compatible(e1, logicalNot(e2));
  }
  private static boolean compatible(final Expression e1, final Expression e2) {
    return e1.getNodeType() == e2.getNodeType() && (e1 instanceof InstanceofExpression || e1 instanceof InfixExpression || e1 instanceof MethodInvocation);
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression _) {
    return "Invert logical condition and exhange order of '?' and ':' operands to conditional expression";
  }
  @Override WringGroup wringGroup() {
	return WringGroup.REORDER_EXPRESSIONS;
  }
}