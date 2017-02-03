package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code (x)->x} to {@code x->x}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LambdaRemoveParenthesis extends ReplaceCurrentNode<LambdaExpression> implements TipperCategory.Inlining {
  @Override protected boolean prerequisite(@NotNull final LambdaExpression ¢) {
    return ¢.hasParentheses() && az.variableDeclrationFragment(onlyOne(parameters(¢))) != null;
  }

  @Override @NotNull public String description(final LambdaExpression ¢) {
    return "Remove parenthesis around " + onlyOne(parameters(¢)) + " paramter";
  }

  @Override @Nullable public LambdaExpression replacement(final LambdaExpression ¢) {
    final LambdaExpression $ = copy.of(¢);
    $.setParentheses(false);
    return $;
  }
}