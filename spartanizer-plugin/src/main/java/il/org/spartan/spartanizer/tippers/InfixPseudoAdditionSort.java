package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.OR;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.XOR;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import il.org.spartan.spartanizer.engine.ExpressionComparator;
import il.org.spartan.spartanizer.tipping.InfixExpressionSortingFull;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** sorts the arguments of an expression using the same sorting order as
 * {@link Operator#PLUS} expression, except that we do not worry about
 * commutativity. Unlike {@link InfixAdditionSort}, we know that the reordering
 * is always possible.
 * @see InfixAdditionSort
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixPseudoAdditionSort extends InfixExpressionSortingFull//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0x4616D89C9896E36AL;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.ADDITION.sort(¢);
  }
  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), OR, XOR, AND);
  }
}
