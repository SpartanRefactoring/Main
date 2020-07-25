package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static il.org.spartan.spartanizer.ast.navigate.extract.allOperands;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_OR;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.sideEffects;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * b && true
 * } to {@code
 * b
 * }
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class InfixConditionalCommon extends ReplaceCurrentNode<InfixExpression>//
    implements Category.CommonFactorOut {
  private static final long serialVersionUID = -0x756F9C11630A8B48L;

  private static Expression chopHead(final InfixExpression ¢) {
    final List<Expression> $ = allOperands(¢);
    $.remove(0);
    return $.size() < 2 ? copy.of(the.firstOf($)) : subject.operands($).to(¢.getOperator());
  }
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Factor out common logical component of ||";
  }
  @Override public Expression replacement(final InfixExpression x) {
    final Operator $ = x.getOperator();
    if (!in($, CONDITIONAL_AND, CONDITIONAL_OR))
      return null;
    final Operator conjugate = op.conjugate($);
    final InfixExpression left = az.infixExpression(core(left(x)));
    if (left == null || left.getOperator() != conjugate)
      return null;
    final InfixExpression right = az.infixExpression(core(right(x)));
    if (right == null || right.getOperator() != conjugate)
      return null;
    final Expression leftLeft = left(left);
    return !sideEffects.free(leftLeft) || !wizard.eq(leftLeft, left(right)) ? null
        : subject.pair(leftLeft, subject.pair(chopHead(left), chopHead(right)).to($)).to(conjugate);
  }
}
