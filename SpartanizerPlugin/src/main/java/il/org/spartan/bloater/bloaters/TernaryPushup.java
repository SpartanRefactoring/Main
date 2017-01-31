package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.safety.iz.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert
 *
 * <code>
 * a + (cond ? b : c)
 *
 * <code>
 * to
 *
 * <code>
 * cond ? a + b : a + c
 *
 * <code>
 * Test case is {@link Issue1049}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-18 */
public class TernaryPushup extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final InfixExpression x) {
    final Expression l = extract.core(left(x)), r = extract.core(right(x));
    if (conditionalExpression(r)) {
      final ConditionalExpression $ = az.conditionalExpression(r);
      return subject.pair(subject.pair(l, then($)).to(operator(x)), subject.pair(l, elze($)).to(operator(x))).toCondition(expression($));
    }
    final ConditionalExpression ll = az.conditionalExpression(l);
    return subject.pair(subject.pair(then(ll), r).to(operator(x)), subject.pair(elze(ll), r).to(operator(x))).toCondition(expression(ll));
  }

  @Override protected boolean prerequisite(final InfixExpression x) {
    if (x == null)
      return false;
    final Expression $ = extract.core(left(x)), r = extract.core(right(x));
    return conditionalExpression(r) && !haz.sideEffects(expression(az.conditionalExpression(r)))
        || conditionalExpression($) && !haz.sideEffects(expression(az.conditionalExpression($)));
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "";
  }
}
