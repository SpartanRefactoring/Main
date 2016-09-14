package il.org.spartan.spartanizer.java;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

// TOOD Niv: Who wrote this class?
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

  // doesn't work for division, need to figure out why
  Expression asExpression() {
    if (!divider)
      return expression;
    final InfixExpression $ = expression.getAST().newInfixExpression();
    $.setOperator(InfixExpression.Operator.DIVIDE);
    $.setLeftOperand(expression.getAST().newNumberLiteral("1"));
    $.setRightOperand(!iz.infixExpression(expression) ? duplicate.of(expression) : make.parethesized(duplicate.of(expression)));
    return $;
  }

  boolean divider() {
    return divider;
  }

  public boolean multiplier() {
    return !divider;
  }
}