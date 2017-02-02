package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixAdditionSort extends InfixExpressionSortingFull//
    implements TipperCategory.Sorting {
  @Override protected boolean sort(@NotNull final List<Expression> ¢) {
    return ExpressionComparator.ADDITION.sort(¢);
  }

  @Override protected boolean suitable(@NotNull final InfixExpression ¢) {
    return in(¢.getOperator(), PLUS2) && type.isNotString(¢);
  }
}
