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

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixMultiplicationSort extends InfixExpressionSortingFull//
    implements TipperCategory.Sorting {
  @Override protected boolean sort(@NotNull final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }

  @Override protected boolean suitable(@NotNull final InfixExpression ¢) {
    return in(¢.getOperator(), TIMES);
  }
}
