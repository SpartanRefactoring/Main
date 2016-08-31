package il.org.spartan.refactoring.java;

import org.eclipse.jdt.core.dom.*;

class Factor {
  private final boolean divider;
  public final Expression expression;

  Factor(final boolean minus, final Expression expression) {
    divider = minus;
    this.expression = expression;
  }

  static Factor times(final Expression e) {
    return new Factor(false, e);
  }

  static Factor divide(final Expression e) {
    return new Factor(true, e);
  }

  boolean divier() {
    return divider;
  }

  Expression asExpression() {
    if (!divider)
      return expression;
    final InfixExpression $ = expression.getAST().newInfixExpression();
    $.setRightOperand(expression);
    $.setOperator(InfixExpression.Operator.DIVIDE);
    $.setLeftOperand(expression.getAST().newNumberLiteral("1"));
    return $;
  }

  public boolean multiplier() {
    return !divider;
  }
}