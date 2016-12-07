package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

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
public final class InfixConditionalCommon extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.CommnoFactoring {
  private static Expression chopHead(final InfixExpression x) {
    final List<Expression> $ = extract.allOperands(x);
    $.remove(0);
    return $.size() < 2 ? duplicate.of(first($)) : subject.operands($).to(x.getOperator());
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
    final Operator $ = x.getOperator();
    if (!in($, CONDITIONAL_AND, CONDITIONAL_OR))
      return null;
    final Operator conjugate = conjugate($);
    final InfixExpression left = az.infixExpression(core(left(x)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = az.infixExpression(core(right(x)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = left(left);
    return haz.sideEffects(leftLeft) || !wizard.same(leftLeft, left(right)) ? null
        : subject.pair(leftLeft, subject.pair(chopHead(left), chopHead(right)).to($)).to(conjugate);
  }
}
