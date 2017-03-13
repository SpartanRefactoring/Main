package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert {@code (x)->x} to {@code x->x}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LambdaRemoveParenthesis extends ReplaceCurrentNode<LambdaExpression> implements TipperCategory.Inlining {
  private static final long serialVersionUID = 4825405081215291398L;

  @Override protected boolean prerequisite(final LambdaExpression ¢) {
    return ¢.hasParentheses() && az.variableDeclrationFragment(onlyOne(parameters(¢))) != null;
  }

  @Override public String description(final LambdaExpression ¢) {
    return "Remove parenthesis around " + onlyOne(parameters(¢)) + " paramter";
  }

  @Override public LambdaExpression replacement(final LambdaExpression ¢) {
    final LambdaExpression $ = copy.of(¢);
    $.setParentheses(false);
    return $;
  }
}
