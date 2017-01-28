package il.org.spartan.spartanizer.tippers;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace
 *
 * <pre>
 * b + -3
 * </pre>
 *
 * To
 *
 * <pre>
 * b - 3
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 05-12-2016 */
public class InfixPlusToMinus extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.SyntacticBaggage {
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.prefixMinus(¢.getRightOperand()) || !iz.infixPlus(¢) ? null
        : subject.pair(¢.getLeftOperand(), az.prefixExpression(¢.getRightOperand()).getOperand()).to(Operator.MINUS);
  }

  @Override public String description(final InfixExpression ¢) {
    return "replace the plus in: " + ¢ + " to minus";
  }
}
