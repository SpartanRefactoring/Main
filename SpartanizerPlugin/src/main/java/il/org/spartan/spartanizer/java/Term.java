package il.org.spartan.spartanizer.java;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

class Term {
  static Term minus(final Expression e) {
    return new Term(true, e);
  }

  static Term plus(final Expression e) {
    return new Term(false, e);
  }

  private final boolean negative;
  public final Expression expression;

  Term(final boolean minus, final Expression expression) {
    negative = minus;
    this.expression = expression;
  }

  public boolean positive() {
    return !negative;
  }

  Expression asExpression() {
    if (!negative)
      return expression;
    final PrefixExpression $ = expression.getAST().newPrefixExpression();
    $.setOperand(expression);
    $.setOperator(wizard.MINUS1);
    return $;
  }

  boolean negative() {
    return negative;
  }
}