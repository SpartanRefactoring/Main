package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert {@code
 * a - b // ==//<//> c - d
 * } into {@code
 * a + d// ==//<//> c + b
 * }
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
public class InfixSimplifyComparisionOfSubtractions extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 1584961763997960580L;

  @Nullable @Override public ASTNode replacement(@NotNull final InfixExpression x) {
    if (!isLiegal(x) || !az.infixExpression(x.getLeftOperand()).extendedOperands().isEmpty()
        || !az.infixExpression(x.getRightOperand()).extendedOperands().isEmpty())
      return null;
    final Expression $ = az.infixExpression(x.getLeftOperand()).getLeftOperand(), lr = az.infixExpression(x.getLeftOperand()).getRightOperand(),
        rl = az.infixExpression(x.getRightOperand()).getLeftOperand(), rr = az.infixExpression(x.getRightOperand()).getRightOperand();
    @Nullable final InfixExpression res = iz.infixExpression(rr) || iz.infixExpression(rl) || iz.infixExpression(lr) || iz.infixExpression($)
        || iz.numberLiteral($) || iz.numberLiteral(lr) || iz.numberLiteral(rl) || iz.numberLiteral(rr) ? null
            : subject.pair(subject.pair($, rr).to(Operator.PLUS), subject.pair(rl, lr).to(Operator.PLUS)).to(x.getOperator());
    return prerequisite(res) ? res : null;
  }

  private static boolean isLiegal(@NotNull final InfixExpression ¢) {
    return isLegalOperation(¢) && iz.infixMinus(¢.getLeftOperand()) && iz.infixMinus(¢.getRightOperand());
  }

  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixEquals(¢) || iz.infixLess(¢) || iz.infixGreater(¢) || iz.infixGreaterEquals(¢) || iz.infixLessEquals(¢);
  }

  @Override public boolean prerequisite(@NotNull final InfixExpression ¢) {
    return new specificity().compare(left(¢), right(¢)) >= 0 || ¢.hasExtendedOperands() || !iz.comparison(¢)
        || !specificity.defined(left(¢)) && !specificity.defined(right(¢));
  }

  @NotNull @Override public String description(final InfixExpression ¢) {
    return "Simplify the comparison expression: " + ¢;
  }
}
