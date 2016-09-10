package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;

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
  private static Expression chopHead(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    es.remove(0);
    return es.size() < 2 ? duplicate.of(lisp.first(es)) : subject.operands(es).to(x.getOperator());
  }

  private static Operator conjugate(final Operator o) {
    return o == null ? null
        : o == CONDITIONAL__AND ? CONDITIONAL__OR //
            : o == CONDITIONAL__OR ? CONDITIONAL__AND //
                : null;
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression ____) {
    return "Factor out common logical component of ||";
  }

  @Override Expression replacement(final InfixExpression x) {
    final Operator o = x.getOperator();
    if (!in(o, CONDITIONAL__AND, CONDITIONAL__OR))
      return null;
    final Operator conjugate = conjugate(o);
    final InfixExpression left = az.infixExpression(core(step.left(x)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = az.infixExpression(core(step.right(x)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = step.left(left);
    return !sideEffects.free(leftLeft) || !wizard.same(leftLeft, step.left(right)) ? null
        : subject.pair(leftLeft, subject.pair(chopHead(left), chopHead(right)).to(o)).to(conjugate);
  }
}
