package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** sorts the arguments of an expression using the same sorting order as
 * {@link Operator#PLUS} expression, except that we do not worry about
 * commutativity. Unlike {@link InfixAdditionSort}, we know that the reordering
 * is always possible.
 * @see InfixAdditionSort
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2015-07-17 */
public final class InfixPseudoAdditionSort extends InfixExpressionSortingFull//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -5050462199229113194L;

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.ADDITION.sort(¢);
  }

  @Override protected boolean suitable(final InfixExpression ¢) {
    return in(¢.getOperator(), OR, XOR, AND);
  }
}
