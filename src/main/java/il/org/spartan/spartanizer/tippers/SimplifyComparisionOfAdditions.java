package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
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
public class SimplifyComparisionOfAdditions extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.Unite {
  @Override public ASTNode replacement(final InfixExpression x) {
    if (!isLegalOperation(x) || !iz.infixExpression(left(x)) || az.infixExpression(left(x)).hasExtendedOperands()
        || iz.numberLiteral(az.infixExpression(left(x)).getLeftOperand()) || !iz.numberLiteral(right(az.infixExpression(left(x)))))
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
    final InfixExpression res = subject.pair($, right).to(operator(x));
    return prerequisite(res) ? res : null;
  }

  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixEquals(¢) || iz.infixLess(¢) || iz.infixGreater(¢) || iz.infixGreaterEquals(¢) || iz.infixLessEquals(¢);
  }

  @Override public boolean prerequisite(final InfixExpression ¢) {
    return new specificity().compare(left(¢), right(¢)) >= 0 || ¢.hasExtendedOperands() || !iz.comparison(¢)
        || !specificity.defined(left(¢)) && !specificity.defined(right(¢));
  }

  @Override public String description(final InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}
