package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert
 *
 * <pre>
 * a <= length - 1
 * </pre>
 *
 * into
 *
 * <pre>
 * a < length
 * </pre>
 *
 * @author Dor Ma'ayan
 * @since 2-12-2016 */
public class LessToLessEquals extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Collapse {
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !isLegalOperation(¢) || !iz.infixPlus(¢.getRightOperand())
        || !"1".equals(az.numberLiteral(az.infixExpression(¢.getRightOperand()).getRightOperand()).getToken())
        || iz.number(az.infixExpression(¢.getRightOperand()).getLeftOperand()) || type.isDouble(¢.getLeftOperand()) ? null
            : subject.pair(¢.getLeftOperand(), az.infixExpression(¢.getRightOperand()).getLeftOperand()).to(Operator.LESS_EQUALS);
  }

  private static boolean isLegalOperation(InfixExpression ¢) {
    return iz.infixLess(¢);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Convert Less Equals Operator to Less " + ¢;
  }
}
