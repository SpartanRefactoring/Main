package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** See class {@link Term}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-02-02 */
class Factor {
  @NotNull static Factor divide(final Expression ¢) {
    return new Factor(true, ¢);
  }

  @NotNull static Factor times(final Expression ¢) {
    return new Factor(false, ¢);
  }

  private final boolean divider;
  public final Expression expression;

  Factor(final boolean divide, final Expression expression) {
    divider = divide;
    this.expression = expression;
  }

  public boolean multiplier() {
    return !divider;
  }

  // doesn'tipper work for division, need to figure out why
  Expression asExpression() {
    if (!divider)
      return expression;
    final InfixExpression $ = expression.getAST().newInfixExpression();
    $.setOperator(DIVIDE);
    $.setLeftOperand(expression.getAST().newNumberLiteral("1"));
    $.setRightOperand(!iz.infixExpression(expression) ? copy.of(expression) : make.parethesized(copy.of(expression)));
    return $;
  }

  boolean divider() {
    return divider;
  }
}