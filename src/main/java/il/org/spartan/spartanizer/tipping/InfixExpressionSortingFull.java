/* TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

public abstract class InfixExpressionSortingFull extends InfixExpressionSorting {
  private static final long serialVersionUID = 0x16A058F976BA7D8EL;

  @Override public final boolean prerequisite(@NotNull final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    @Nullable final List<Expression> $ = extract.allOperands(¢);
    return !Tricks.mixedLiteralKind($) && sort($);
  }

  @Override @Nullable public Expression replacement(@NotNull final InfixExpression ¢) {
    @Nullable final List<Expression> $ = extract.allOperands(¢);
    return !sort($) ? null : subject.operands($).to(¢.getOperator());
  }
}
