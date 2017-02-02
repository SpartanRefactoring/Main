/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InfixExpressionSortingFull extends InfixExpressionSorting {
  @Override public final boolean prerequisite(@NotNull final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    final List<Expression> $ = extract.allOperands(¢);
    return !Tippers.mixedLiteralKind($) && sort($);
  }

  @Override @Nullable public Expression replacement(@NotNull final InfixExpression ¢) {
    final List<Expression> $ = extract.allOperands(¢);
    return !sort($) ? null : subject.operands($).to(¢.getOperator());
  }
}
