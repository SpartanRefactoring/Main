package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts the arguments of a {@link Operator#DIVIDE} expression.
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixDivisonSortRest extends InfixExpressionSortingRest//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -0xA1918BAA5497A0FL;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }
  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), DIVIDE);
  }
}
