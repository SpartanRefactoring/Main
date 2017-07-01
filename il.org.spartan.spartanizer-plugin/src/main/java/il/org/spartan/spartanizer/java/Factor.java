package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

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
    final InfixExpression ret = expression.getAST().newInfixExpression();
    ret.setOperator(DIVIDE);
    ret.setLeftOperand(expression.getAST().newNumberLiteral("1"));
    ret.setRightOperand(!iz.infixExpression(expression) ? copy.of(expression) : make.parethesized(copy.of(expression)));
    return ret;
  }
  boolean divider() {
    return divider;
  }
}