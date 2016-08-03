package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} that sorts the arguments of a {@link Operator#PLUS}
 * expression. Extra care is taken to leave intact the use of
 * {@link Operator#PLUS} for the concatenation of {@link String}s.
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixSortAddition extends Wring.InfixSorting {
  @Override boolean sort(final List<Expression> es) {
    return ExpressionComparator.ADDITION.sort(es);
  }
  @Override boolean eligible(final InfixExpression e) {
    return Is.notString(e) && super.eligible(e);
  }
  @Override Expression replacement(final InfixExpression e) {
    final List<Expression> operands = extract.allOperands(e);
    final boolean notString = Is.notString(e);
    final boolean canSort = sort(operands);
    return !notString || !canSort ? null : Subject.operands(operands).to(e.getOperator());
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return e.getOperator() == PLUS;
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}