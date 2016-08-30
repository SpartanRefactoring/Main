package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.navigate.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.utils.*;

/** convert
 *
 * <pre>
 * b &amp;&amp; true
 * </pre>
 *
 * to
 *
 * <pre>
 * b
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class InfixConditionalCommon extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static Expression chopHead(final InfixExpression e) {
    final List<Expression> es = extract.allOperands(e);
    es.remove(0);
    return es.size() < 2 ? wizard.duplicate(lisp.first(es)) : subject.operands(es).to(e.getOperator());
  }

  private static Operator conjugate(final Operator o) {
    return o == null ? null
        : o == CONDITIONAL_AND ? CONDITIONAL_OR //
            : o == CONDITIONAL_OR ? CONDITIONAL_AND //
                : null;
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Factor out common logical component of ||";
  }

  @Override Expression replacement(final InfixExpression e) {
    final Operator o = e.getOperator();
    if (!in(o, CONDITIONAL_AND, CONDITIONAL_OR))
      return null;
    final Operator conjugate = conjugate(o);
    final InfixExpression left = az.infixExpression(core(left(e)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = az.infixExpression(core(right(e)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = left(left);
    return !sideEffects.free(leftLeft) || !wizard.same(leftLeft, left(right)) ? null
        : subject.pair(leftLeft, subject.pair(chopHead(left), chopHead(right)).to(o)).to(conjugate);
  }
}
