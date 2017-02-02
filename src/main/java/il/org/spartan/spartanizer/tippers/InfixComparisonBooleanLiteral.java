package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.factory.make.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** eliminates redundant comparison with <code><b>true</b> </code> and
 * <code><b>false</b></code> .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonBooleanLiteral extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onBooleans {
  @Nullable
  private static BooleanLiteral literal(final InfixExpression ¢) {
    return az.booleanLiteral(core(literalOnLeft(¢) ? left(¢) : right(¢)));
  }

  private static boolean literalOnLeft(final InfixExpression ¢) {
    return iz.booleanLiteral(core(left(¢)));
  }

  private static boolean literalOnRight(final InfixExpression ¢) {
    return iz.booleanLiteral(core(right(¢)));
  }

  private static boolean negating(@NotNull final InfixExpression x, @NotNull final BooleanLiteral l) {
    return l.booleanValue() != (x.getOperator() == EQUALS);
  }

  @Nullable
  private static Expression nonLiteral(final InfixExpression ¢) {
    return literalOnLeft(¢) ? right(¢) : left(¢);
  }

  @NotNull
  @Override public String description(final InfixExpression ¢) {
    return "Omit redundant comparison with '" + literal(¢) + "'";
  }

  @Override public boolean prerequisite(@NotNull final InfixExpression ¢) {
    return !¢.hasExtendedOperands() && in(¢.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(¢) || literalOnRight(¢));
  }

  @NotNull
  @Override public Expression replacement(@NotNull final InfixExpression x) {
    final BooleanLiteral $ = literal(x);
    final Expression nonliteral = core(nonLiteral(x));
    return plant(!negating(x, $) ? nonliteral : make.notOf(nonliteral)).into(x.getParent());
  }
}
