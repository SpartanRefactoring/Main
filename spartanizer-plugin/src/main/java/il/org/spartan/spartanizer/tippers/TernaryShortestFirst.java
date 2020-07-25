package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.LongestCommonSubsequence;

/** convert {@code
 * a ? (f,g,h) : c(d,e)
 * } into {@code
 * a ? c(d, e) : f(g, h)
 * }
 * @author Yossi Gil
 * @since 2015-08-14 */
public final class TernaryShortestFirst extends ReplaceCurrentNode<ConditionalExpression>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0x6BB1DE73712953FEL;

  private static double align(final Expression e1, final Expression e2) {
    return new LongestCommonSubsequence(e1 + "", e2 + "").similarity();
  }
  private static boolean compatible(final Expression e1, final Expression e2) {
    return e1.getNodeType() == e2.getNodeType()
        && (e1 instanceof InstanceofExpression || e1 instanceof InfixExpression || e1 instanceof MethodInvocation);
  }
  private static boolean compatibleCondition(final Expression e1, final Expression e2) {
    return compatible(e1, e2) || compatible(e1, cons.not(e2));
  }
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Invert logical condition and exhange order of '?' and ':' operands to conditional expression";
  }
  @Override public ConditionalExpression replacement(final ConditionalExpression x) {
    final ConditionalExpression $ = subject.pair(elze(x), then(x)).toCondition(cons.not(x.getExpression()));
    final Expression then = elze($), elze = then($);
    if (!iz.conditionalExpression(then) && iz.conditionalExpression(elze))
      return null;
    if (iz.conditionalExpression(then) && !iz.conditionalExpression(elze))
      return $;
    final ConditionalExpression parent = az.conditionalExpression(x.getParent());
    if (parent != null && parent.getElseExpression() == x && compatibleCondition(parent.getExpression(), x.getExpression())) {
      final Expression alignTo = parent.getThenExpression();
      final double a1 = align(elze, alignTo), a2 = align(then, alignTo);
      if (Math.abs(a1 - a2) > 0.1)
        return a1 > a2 ? $ : null;
    }
    final Expression condition = cons.not($.getExpression());
    return Metrics.length(condition, then) > Metrics.length(cons.not(condition), elze) ? $ : null;
  }
}
