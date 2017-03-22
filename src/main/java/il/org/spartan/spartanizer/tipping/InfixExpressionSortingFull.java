/* TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
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
  private static final long serialVersionUID = 1630400893570153870L;

  @Override public final boolean prerequisite(@NotNull final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    @Nullable final List<Expression> $ = extract.allOperands(¢);
    return !Tippers.mixedLiteralKind($) && sort($);
  }

  @Nullable @Override public Expression replacement(@NotNull final InfixExpression ¢) {
    @Nullable final List<Expression> $ = extract.allOperands(¢);
    return !sort($) ? null : subject.operands($).to(¢.getOperator());
  }
}
