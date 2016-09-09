package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.engine.*;

/** sorts the arguments of a {@link Operator#DIVIDE} expression.
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixDivisonSort extends Wring.InfixSortingOfCDR implements Kind.Sorting {
  @Override boolean scopeIncludes(final InfixExpression x) {
    return in(x.getOperator(), DIVIDE);
  }

  @Override boolean sort(final List<Expression> xs) {
    return ExpressionComparator.MULTIPLICATION.sort(xs);
  }
}
