package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.iz;

/** @see Term
 * @author Yossi Gil
 * @since 2017-02-02 */
class Factor {
  static Factor divide(final Expression ¢) {
    return new Factor(true, ¢);
  }
  static Factor times(final Expression ¢) {
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
    $.setRightOperand(!iz.infixExpression(expression) ? copy.of(expression) : subject.operand(copy.of(expression)).parenthesis());
    return $;
  }
  boolean divider() {
    return divider;
  }
}