package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.engine.*;

/** sorts the arguments of an expression using the same sorting order as
 * {@link Operator#PLUS} expression, except that we do not worry about
 * commutativity. Unlike {@link InfixAdditionSort}, we know that the reordering
 * is always possible.
 * @see InfixAdditionSort
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixPseudoAdditionSort extends Wring.InfixSorting implements Kind.Sorting {
  @Override boolean scopeIncludes(final InfixExpression e) {
    return in(e.getOperator(), OR, XOR, AND);
  }

  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.ADDITION.sort(es);
  }
}
