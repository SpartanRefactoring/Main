package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert {@code
 * a <= length - 1
 * } into {@code
 * a < length
 * }
 * @author Dor Ma'ayan
 * @since 2-12-2016 */
public class InfixLessEqualsToLess extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Arithmetics {
  private static final long serialVersionUID = 0x33256C45179502ABL;

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !isLegalOperation(¢)//
        || !iz.infixMinus(right(¢))//
        || !"1".equals(token(az.numberLiteral(right(az.infixExpression(right(¢)))))) || type.isDouble(¢.getLeftOperand()) ? null
            : subject.pair(left(¢), left(az.infixExpression(right(¢)))).to(Operator.LESS);
  }
  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixLessEquals(¢);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Convert '<=' operator to '<' " + ¢;
  }
}
