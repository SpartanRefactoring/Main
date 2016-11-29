package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts
 *
 * <pre>
 * x == y ? y : x
 * </pre>
 *
 * into
 *
 * <pre>
 * x
 * </pre>
 *
 * @author Dan Abramovich
 * @since 27-11-2016 */
public class SameEvaluationConditional extends ReplaceCurrentNode<ConditionalExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final ConditionalExpression x) {
    final InfixExpression cond = az.infixExpression(x.getExpression());
    return !iz.infixEquals(cond)
        || !wizard.same(x.getThenExpression(), cond.getLeftOperand()) && !wizard.same(x.getThenExpression(), cond.getRightOperand())
        || !wizard.same(x.getElseExpression(), cond.getLeftOperand()) && !wizard.same(x.getElseExpression(), cond.getRightOperand())
        || !sideEffects.free(cond.getLeftOperand()) || !sideEffects.free(cond.getRightOperand()) ? null : duplicate.of(x.getElseExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression ¢) {
    return "eliminate ternary expression that evaluates to the same value";
  }
}
