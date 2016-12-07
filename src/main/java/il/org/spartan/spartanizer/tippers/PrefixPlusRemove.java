package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.make.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code>int i = +0</code> with <code>int i = 0</code>,
 * <code>int i = +1</code> with <code>int i = 1</code> <code>int i = +a</code>
 * with <code>int i = a</code>, etc.
 * @author Matteo Orru'
 * @since 2016 */
public final class PrefixPlusRemove extends ReplaceCurrentNode<PrefixExpression> implements TipperCategory.InVain {
  @Override public String description(final PrefixExpression ¢) {
    return "Remove unary + in " + ¢;
  }

  @Override public ASTNode replacement(final PrefixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : plant(duplicate.of(heart(¢.getOperand()))).into(¢.getParent());
  }

  private Expression heart(final Expression x) {
    if (iz.nodeTypeEquals(x, PARENTHESIZED_EXPRESSION))
      return heart(step.expression(x));
    final PrefixExpression $ = az.prefixExpression(x);
    return $ == null || $.getOperator() != PLUS ? x : heart($.getOperand());
  }
}
