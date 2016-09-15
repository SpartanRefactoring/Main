package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Replace <code>int i = +0</code> with <code>int i = 0</code>,
 * <code>int i = +1</code> with <code>int i = 1</code> <code>int i = +a</code>
 * with <code>int i = a</code>, etc.
 * @author Matteo Orru'
 * @since 2016 */
public class PrefixPlusRemove extends ReplaceCurrentNode<PrefixExpression> implements Kind.NOP {
  @Override public String description(final PrefixExpression ¢) {
    return "Remove unary + in " + ¢;
  }

  private Expression heart(final Expression x) {
    if (iz.is(x, PARENTHESIZED_EXPRESSION))
      return heart(step.expression(x));
    final PrefixExpression p = az.prefixExpression(x);
    return p == null || p.getOperator() != PLUS ? x : heart(p.getOperand());
  }

  @Override public ASTNode replacement(final PrefixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : il.org.spartan.spartanizer.assemble.make.plant(duplicate.of(heart(¢.getOperand()))).into(¢.getParent());
  }
}
