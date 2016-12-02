package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert
 *
 * <pre>
 * a + 2 // ==//<//> 3
 * </pre>
 *
 * into
 *
 * <pre>
 * a // ==//<//> 1
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
public class SimplifyComparisionOfAdditions extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final InfixExpression x) {
    if (!isLegalOperation(x) || az.infixExpression(x.getLeftOperand()).hasExtendedOperands()
        || iz.numberLiteral(az.infixExpression(x.getLeftOperand()).getLeftOperand())
        || !iz.numberLiteral(az.infixExpression(x.getLeftOperand()).getRightOperand()))
      return null;
    final Expression left = az.infixExpression(x.getLeftOperand()).getLeftOperand();
    Expression right;
    if (iz.infixPlus(x.getLeftOperand()))
      right = subject.pair(x.getRightOperand(), az.infixExpression(x.getLeftOperand()).getRightOperand()).to(Operator.MINUS);
    else {
      if (!iz.infixMinus(x.getLeftOperand()))
        return null;
      right = subject.pair(x.getRightOperand(), az.infixExpression(x.getLeftOperand()).getRightOperand()).to(Operator.PLUS);
    }
    return subject.pair(left, right).to(x.getOperator());
  }
  
  private static boolean isLegalOperation(InfixExpression ¢){
    return iz.infixEquals(¢) ||
        iz.infixLess(¢) ||
        iz.infixGreater(¢) ||
        iz.infixGreaterEquals(¢) ||
        iz.infixLessEquals(¢);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}
