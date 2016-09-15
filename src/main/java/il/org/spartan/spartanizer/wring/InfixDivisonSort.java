package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** sorts the arguments of a {@link Operator#DIVIDE} expression.
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixDivisonSort extends InfixSortingOfCDR implements Kind.Sorting {
  @Override public boolean demandsToSuggestButPerhapsCant(final InfixExpression ¢) {
    return in(¢.getOperator(), DIVIDE);
  }

  @Override protected boolean sort(final List<Expression> ¢) {
    return ExpressionComparator.MULTIPLICATION.sort(¢);
  }
}
