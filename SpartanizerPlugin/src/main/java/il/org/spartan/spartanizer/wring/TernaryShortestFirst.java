package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.utils.*;

/** convert
 *
 * <pre>
 * a ? (f,g,h) : c(d,e)
 * </pre>
 *
 * into
 *
 * <pre>
 * a ? c(d, e) : f(g, h)
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-14 */
public final class TernaryShortestFirst extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Canonicalization {
  private static double align(final Expression e1, final Expression e2) {
    return new LongestCommonSubsequence(e1 + "", e2 + "").similarity();
  }

  private static boolean compatible(final Expression e1, final Expression e2) {
    return e1.getNodeType() == e2.getNodeType()
        && (e1 instanceof InstanceofExpression || e1 instanceof InfixExpression || e1 instanceof MethodInvocation);
  }

  private static boolean compatibleCondition(final Expression e1, final Expression e2) {
    return compatible(e1, e2) || compatible(e1, make.notOf(e2));
  }

  @Override String description(@SuppressWarnings("unused") final ConditionalExpression ____) {
    return "Invert logical condition and exhange order of '?' and ':' operands to conditional expression";
  }

  @Override ConditionalExpression replacement(final ConditionalExpression x) {
    final ConditionalExpression $ = subject.pair(core(x.getElseExpression()), core(x.getThenExpression())).toCondition(make.notOf(x.getExpression()));
    final Expression then = $.getElseExpression();
    final Expression elze = $.getThenExpression();
    if (!iz.conditional(then) && iz.conditional(elze))
      return null;
    if (iz.conditional(then) && !iz.conditional(elze))
      return $;
    final ConditionalExpression parent = az.conditionalExpression(x.getParent());
    if (parent != null && parent.getElseExpression() == x && compatibleCondition(parent.getExpression(), x.getExpression())) {
      final Expression alignTo = parent.getThenExpression();
      final double a1 = align(elze, alignTo);
      final double a2 = align(then, alignTo);
      if (Math.abs(a1 - a2) > 0.1)
        return a1 > a2 ? $ : null;
    }
    final Expression condition = make.notOf($.getExpression());
    return Wrings.length(condition, then) > Wrings.length(make.notOf(condition), elze) ? $ : null;
  }
}
