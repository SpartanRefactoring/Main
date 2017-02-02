/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;

abstract class InfixExpressionSorting extends ReplaceCurrentNode<InfixExpression> {
  @NotNull
  @Override public final String description(@NotNull final InfixExpression ¢) {
    return "Reorder operands of " + ¢.getOperator();
  }

  protected abstract boolean sort(List<Expression> operands);

  protected abstract boolean suitable(InfixExpression x);
}
