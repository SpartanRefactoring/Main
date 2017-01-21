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
public class SameEvaluationConditional extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.Unite {
  @Override public ASTNode replacement(final ConditionalExpression ¢) {
    final InfixExpression $ = az.infixExpression(¢.getExpression());
    return !iz.infixEquals($) || !wizard.same(¢.getThenExpression(), $.getLeftOperand()) && !wizard.same(¢.getThenExpression(), $.getRightOperand())
        || !wizard.same(¢.getElseExpression(), $.getLeftOperand()) && !wizard.same(¢.getElseExpression(), $.getRightOperand())
        || !sideEffects.free($.getLeftOperand()) || !sideEffects.free($.getRightOperand()) ? null : copy.of(¢.getElseExpression());
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression ¢) {
    return "eliminate ternary expression that evaluates to the same value";
  }
}
