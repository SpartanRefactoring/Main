package il.org.spartan.refactoring.wring;

import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} that sorts the arguments of an expression using the same
 * sorting order as {@link Operator#PLUS} expression, except that we do not
 * worry about commutativity. Unlike {@link InfixSortAddition}, we know that the
 * reordering is always possible.
 * @see InfixSortAddition
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixSortPseudoAddition extends Wring.InfixSorting implements Kind.Sorting {
  @Override boolean scopeIncludes(final InfixExpression e) {
    return in(e.getOperator(), OR, XOR, AND);
  }
  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.ADDITION.sort(es);
  }
}
