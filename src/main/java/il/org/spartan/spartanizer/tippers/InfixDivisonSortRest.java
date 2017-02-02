package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** sorts the arguments of a {@link Operator#DIVIDE} expression.
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixDivisonSortRest extends InfixExpressionSortingRest//
    implements TipperCategory.Sorting {
  @Override protected boolean sort(@NotNull final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }

  @Override protected boolean suitable(@NotNull final InfixExpression ¢) {
    return in(¢.getOperator(), DIVIDE);
  }
}
