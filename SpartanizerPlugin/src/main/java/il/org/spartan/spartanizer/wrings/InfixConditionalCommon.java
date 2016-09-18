package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wringing.*;

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
public final class InfixConditionalCommon extends ReplaceCurrentNode<InfixExpression> implements Kind.DistributiveRefactoring {
  private static Expression chopHead(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    es.remove(0);
    return es.size() < 2 ? duplicate.of(first(es)) : subject.operands(es).to(x.getOperator());
  }

  private static Operator conjugate(final Operator ¢) {
    return ¢ == null ? null
        : ¢ == CONDITIONAL_AND ? CONDITIONAL_OR //
            : ¢ == CONDITIONAL_OR ? CONDITIONAL_AND //
                : null;
  }

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Factor out common logical component of ||";
  }

  @Override public Expression replacement(final InfixExpression x) {
    final Operator o = x.getOperator();
    if (!in(o, CONDITIONAL_AND, CONDITIONAL_OR))
      return null;
    final Operator conjugate = conjugate(o);
    final InfixExpression left = az.infixExpression(core(left(x)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = az.infixExpression(core(right(x)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = left(left);
    return !sideEffects.free(leftLeft) || !wizard.same(leftLeft, left(right)) ? null
        : subject.pair(leftLeft, subject.pair(chopHead(left), chopHead(right)).to(o)).to(conjugate);
  }
}
