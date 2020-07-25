package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import org.eclipse.jdt.core.dom.LambdaExpression;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code (x)->x} to {@code x->x}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LambdaRemoveParenthesis extends ReplaceCurrentNode<LambdaExpression> implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x42F7485AF333D006L;

  @Override protected boolean prerequisite(final LambdaExpression ¢) {
    return ¢.hasParentheses() && az.variableDeclrationFragment(the.onlyOneOf(parameters(¢))) != null;
  }
  @Override public String description(final LambdaExpression ¢) {
    return "Remove parenthesis around " + the.onlyOneOf(parameters(¢)) + " paramter";
  }
  @Override public LambdaExpression replacement(final LambdaExpression ¢) {
    final LambdaExpression $ = copy.of(¢);
    $.setParentheses(false);
    return $;
  }
}
