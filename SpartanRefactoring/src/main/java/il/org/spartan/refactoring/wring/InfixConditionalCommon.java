package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>b && true</code> to <code>b</code>
 *
 * @author Yossi Gil
 * @since 2015-07-20
 */
public final class InfixConditionalCommon extends Wring.ReplaceCurrentNode<InfixExpression> {
  @Override Expression replacement(final InfixExpression e) {
    final Operator o = e.getOperator();
    if (!in(o, CONDITIONAL_AND, CONDITIONAL_OR))
      return null;
    final Operator conjugate = conjugate(o);
    final InfixExpression left = asInfixExpression(core(left(e)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = asInfixExpression(core(right(e)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = left(left);
    return !Is.sideEffectFree(leftLeft) || !same(leftLeft, left(right)) ? null
        : Subject.pair(leftLeft, Subject.pair(chopHead(left), chopHead(right)).to(o)).to(conjugate);
  }
  private static Operator conjugate(final Operator o) {
    return o == null ? null
        : o == CONDITIONAL_AND ? CONDITIONAL_OR //
            : o == CONDITIONAL_OR ? CONDITIONAL_AND //
                : null;
  }
  private static Expression chopHead(final InfixExpression e) {
    final List<Expression> es = allOperands(e);
    es.remove(0);
    return es.size() < 2 ? duplicate(es.get(0)) : Subject.operands(es).to(e.getOperator());
  }
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Factor out common logical component of ||";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}