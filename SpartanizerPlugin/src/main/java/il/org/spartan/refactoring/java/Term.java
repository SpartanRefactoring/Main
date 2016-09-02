package il.org.spartan.refactoring.java;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;

class Term {
  private final boolean negative;
  public final Expression expression;

  Term(final boolean minus, final Expression expression) {
    negative = minus;
    this.expression = expression;
  }

  static Term plus(final Expression x) {
    return new Term(false, x);
  }

  static Term minus(final Expression x) {
    return new Term(true, x);
  }

  boolean negative() {
    return negative;
  }

  Expression asExpression() {
    if (!negative)
      return expression;
    final PrefixExpression $ = expression.getAST().newPrefixExpression();
    $.setOperand(expression);
    $.setOperator(wizard.MINUS1);
    return $;
  }

  public boolean positive() {
    return !negative;
  }
}