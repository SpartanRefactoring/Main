package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.java.*;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixAdditionSort extends Wring.InfixSorting implements Kind.Canonicalization {
  @Override boolean canMake(final InfixExpression e) {
    return iz . notString(e) && super.canMake(e);
  }

  @Override Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    return !stringType.isNot(x) || !sort(operands) ? null : subject.operands(operands).to(x.getOperator());
  }

  @Override boolean claims(final InfixExpression e) {
    return e.getOperator() == PLUS;
  }

  @Override boolean sort(final List<Expression> xs) {
    return ExpressionComparator.ADDITION.sort(xs);
  }
}
