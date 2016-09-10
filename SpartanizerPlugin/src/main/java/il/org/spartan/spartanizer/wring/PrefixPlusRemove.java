package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.assemble.plant.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>int i = +0</code> with <code>int i = 0</code>,
 * <code>int i = +1</code> with <code>int i = 1</code> <code>int i = +a</code>
 * with <code>int i = a</code>, etc.
 * @author Matteo Orru'
 * @since 2016 */
public class PrefixPlusRemove extends ReplaceCurrentNode<PrefixExpression> implements Kind.NoImpact {
  @Override String description(final PrefixExpression x) {
    return "Remove unary + in " + x;
  }

  @Override ASTNode replacement(final PrefixExpression x) {
    return x.getOperator() != PLUS ? null : plant(duplicate.of(heart(x.getOperand()))).into(x.getParent());
  }

  private Expression heart(final Expression x) {
    if (iz.is(x, PARENTHESIZED__EXPRESSION))
      return heart(step.expression(x));
    final PrefixExpression p = az.prefixExpression(x);
    return p == null || p.getOperator() != PLUS ? x : heart(p.getOperand());
  }
}
