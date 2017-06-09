package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert {@code
 * a + 2 // ==//<//> 3
 * } into {@code
 * a // ==//<//> 1
 * }
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
public class InfixSimplifyComparisionOfAdditions extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Arithmetics.Symbolic {
  private static final long serialVersionUID = 0x6943E66FD8769182L;

  @Override public ASTNode replacement(final InfixExpression x) {
    if (!isLegalOperation(x) || !iz.infixExpression(left(x)) || az.infixExpression(left(x)).hasExtendedOperands()
        || iz.numberLiteral(az.infixExpression(left(x)).getLeftOperand()) || !iz.numberLiteral(right(az.infixExpression(left(x)))))
      return null;
    final Expression $ = left(az.infixExpression(left(x))), right;
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
