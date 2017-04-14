package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code
 * b + -3
 * } To {@code
 * b - 3
 * }
 * @author Dor Ma'ayan
 * @since 05-12-2016 */
public class InfixPlusToMinus extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x2637AF187963DB3FL;

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.prefixMinus(¢.getRightOperand()) || !iz.infixPlus(¢) ? null
        : subject.pair(¢.getLeftOperand(), az.prefixExpression(¢.getRightOperand()).getOperand()).to(Operator.MINUS);
  }

  @Override public String description(final InfixExpression ¢) {
    return "replace the plus in: " + ¢ + " to minus";
  }
}
