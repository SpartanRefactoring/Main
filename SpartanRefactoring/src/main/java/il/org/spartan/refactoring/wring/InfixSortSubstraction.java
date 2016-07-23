package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

/**
 * A {@link Wring} that sorts the arguments of a {@link Operator#PLUS}
 * expression. Extra care is taken to leave intact the use of
 * {@link Operator#PLUS} for the concatenation of {@link String}s.
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class InfixSortSubstraction extends Wring.InfixSortingOfCDR implements Kind.ReorganizeExpression {
  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.ADDITION.sort(es);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return in(e.getOperator(), MINUS);
  }
}