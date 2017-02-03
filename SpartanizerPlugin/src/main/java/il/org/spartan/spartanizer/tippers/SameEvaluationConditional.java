package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** converts {@code x==y?y:x} into {@code x}
 * @author Dan Abramovich
 * @since 27-11-2016 */
public class SameEvaluationConditional extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.EmptyCycles {
  @Override @Nullable public ASTNode replacement(@NotNull final ConditionalExpression ¢) {
    return copy.of(¢.getElseExpression());
  }

  @Override protected boolean prerequisite(@NotNull final ConditionalExpression x) {
    final InfixExpression $ = az.infixExpression(x.getExpression());
    if (!iz.infixEquals($))
      return false;
    final Expression left = $.getLeftOperand();
    if (!sideEffects.free(left))
      return false;
    final Expression right = $.getRightOperand();
    if (!sideEffects.free(right))
      return false;
    final Expression then = x.getThenExpression();
    if (!wizard.same(then, left) && !wizard.same(then, right))
      return false;
    final Expression elze = x.getElseExpression();
    return wizard.same(elze, left) || wizard.same(elze, right);
  }

  @Override @NotNull public String description(@SuppressWarnings("unused") final ConditionalExpression ¢) {
    return "eliminate ternary expression that evaluates to the same value";
  }
}