package org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.spartan.utils.Utils.in;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.spartan.refactoring.utils.ExpressionComparator;

/**
 * A {@link Wring} that sorts the arguments of a {@link Operator#DIVIDE}
 * expression.
 *
 * @author Yossi Gil
 * @since 2015-09-05
 */
public final class InfixSortDivision extends Wring.InfixSortingOfCDR {
  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.MULTIPLICATION.sort(es);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return in(e.getOperator(), DIVIDE);
  }
}