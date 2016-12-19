package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

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
    if (!isLegalOperation(x)//
        || !iz.infixExpression(left(x))//
        || az.infixExpression(left(x)).hasExtendedOperands()//
        || iz.numberLiteral(az.infixExpression(left(x)).getLeftOperand())//
        || !iz.numberLiteral(right(az.infixExpression(left(x)))))
      return null;
    final Expression $ = left(az.infixExpression(left(x)));
    Expression right;
    if (iz.infixPlus(left(x)))
      right = subject.pair(right(x), right(az.infixExpression(left(x)))).to(Operator.MINUS);
    else {
      if (!iz.infixMinus(left(x)))
        return null;
      right = subject.pair(right(x), right(az.infixExpression(left(x)))).to(Operator.PLUS);
    }
    return subject.pair($, right).to(operator(x));
  }

  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixEquals(¢) || iz.infixLess(¢) || iz.infixGreater(¢) || iz.infixGreaterEquals(¢) || iz.infixLessEquals(¢);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}
