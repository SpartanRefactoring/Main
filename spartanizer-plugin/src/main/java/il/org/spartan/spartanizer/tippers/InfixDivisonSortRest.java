package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import il.org.spartan.spartanizer.engine.ExpressionComparator;
import il.org.spartan.spartanizer.tipping.InfixExpressionSortingRest;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** sorts the arguments of a {@link Operator#DIVIDE} expression.
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixDivisonSortRest extends InfixExpressionSortingRest//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0xA1918BAA5497A0FL;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }
  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), DIVIDE);
  }
}
