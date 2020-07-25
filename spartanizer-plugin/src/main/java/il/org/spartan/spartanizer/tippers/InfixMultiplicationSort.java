package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import il.org.spartan.spartanizer.engine.ExpressionComparator;
import il.org.spartan.spartanizer.tipping.InfixExpressionSortingFull;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixMultiplicationSort extends InfixExpressionSortingFull//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = -0x503C45E0EC609DAEL;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }
  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), TIMES);
  }
}
