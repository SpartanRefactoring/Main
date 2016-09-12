package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** sorts the arguments of a {@link Operator#PLUS} expression. Extra care is
 * taken to leave intact the use of {@link Operator#PLUS} for the concatenation
 * of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixAdditionSort extends Wring.InfixSorting implements Kind.Collapse {
  @Override boolean eligible(final InfixExpression x) {
    return stringType.isNot(x) && super.eligible(x);
  }

  @Override Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    return !stringType.isNot(x) || !sort(operands) ? null : subject.operands(operands).to(x.getOperator());
  }

  @Override boolean scopeIncludes(final InfixExpression x) {
    return x.getOperator() == PLUS;
  }

  @Override boolean sort(final List<Expression> xs) {
    return ExpressionComparator.ADDITION.sort(xs);
  }
}
