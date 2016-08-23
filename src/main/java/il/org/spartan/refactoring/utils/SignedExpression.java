package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

class SignedExpression {
  private final boolean negative;
  public final Expression expression;

  SignedExpression(final boolean minus, final Expression expression) {
    negative = minus;
    this.expression = expression;
  }

  static SignedExpression plus(final Expression e) {
    return new SignedExpression(false, e);
  }

  static SignedExpression minus(final Expression e) {
    return new SignedExpression(true, e);
  }

  boolean negative() {
    return negative;
  }

  Expression asExpression() {
    if (!negative)
      return expression;
    final PrefixExpression $ = expression.getAST().newPrefixExpression();
    $.setOperand(expression);
    $.setOperator(MINUS1);
    return $;
  }

  public boolean positive() {
    return !negative;
  }
}