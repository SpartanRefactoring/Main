/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tipping;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

public abstract class InfixExpressionSortingFull extends InfixExpressionSorting {
  private static final long serialVersionUID = 0x16A058F976BA7D8EL;

  @Override public final boolean prerequisite(final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    final List<Expression> $ = extract.allOperands(¢);
    return !misc.mixedLiteralKind($) && sideEffectSafe($) && sort($);
  }
  private static boolean sideEffectSafe(final List<Expression> $) {
    return $.stream().allMatch(sideEffects::free) || $.stream().filter(haz::sideEffects).count() <= 1
        && $.stream().filter(sideEffects::free).collect(Collectors.toList()).stream().allMatch(InfixExpressionSortingFull::literalsExpression);
  }
  static boolean literalsExpression(final Expression ¢) {
    return iz.literal(¢)
        || iz.infixExpression(¢) && extract.allOperands(az.infixExpression(¢)).stream().allMatch(InfixExpressionSortingFull::literalsExpression);
  }
  @Override public Expression replacement(final InfixExpression ¢) {
    final List<Expression> $ = extract.allOperands(¢);
    return !sort($) ? null : subject.operands($).to(¢.getOperator());
  }
}
