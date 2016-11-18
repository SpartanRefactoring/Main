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
 * a + 2 == 3
 * </pre>
 * 
 * into
 * 
 * <pre>
 * a == 1
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
public class SimplifyComparisionOfAdditions extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(InfixExpression ¢) {
    return !iz.infixEquals(¢) || !iz.infixPlus(¢.getLeftOperand())
        || !iz.numberLiteral(az.infixExpression(¢.getLeftOperand()).getRightOperand())
            ? null
            : subject
                .pair(az.infixExpression(¢.getLeftOperand()).getLeftOperand(),
                    subject.pair(¢.getRightOperand(), az.infixExpression(¢.getLeftOperand()).getRightOperand()).to(Operator.MINUS))
                .to(Operator.EQUALS);
  }

  @Override public String description(InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}
