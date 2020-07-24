package il.org.spartan.spartanizer.java;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;

/** An additive term, which might be either negative or positive
 * @author Yossi Gil
 * @since 2016 */
class Term {
  static Term minus(final Expression ¢) {
    return new Term(true, ¢);
  }
  static Term plus(final Expression ¢) {
    return new Term(false, ¢);
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
    $.setOperator(il.org.spartan.spartanizer.ast.navigate.op.MINUS1);
    return $;
  }
  boolean negative() {
    return negative;
  }
}