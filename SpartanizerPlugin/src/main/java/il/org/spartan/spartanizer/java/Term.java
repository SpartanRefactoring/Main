package il.org.spartan.spartanizer.java;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** An additive term, which might be either negative or positive
 * @author Yossi Gil
 * @since 2016 */
class Term {
  static Term minus(final Expression x) {
    return new Term(true, x);
  }

  static Term plus(final Expression x) {
    return new Term(false, x);
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