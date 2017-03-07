/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

abstract class InfixExpressionSorting extends ReplaceCurrentNode<InfixExpression> {
  private static final long serialVersionUID = 2767714386379462412L;

  @Override public final String description(final InfixExpression ¢) {
    return "Reorder operands of " + ¢.getOperator();
  }

  protected abstract boolean sort(List<Expression> operands);

  protected abstract boolean suitable(InfixExpression x);
}
