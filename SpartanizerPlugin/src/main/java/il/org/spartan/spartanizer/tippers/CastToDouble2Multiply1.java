package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.idiomatic.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Replace {@code (double)X} by {@code 1.*X}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToDouble2Multiply1 extends ReplaceCurrentNode<CastExpression>//
    implements TipperCategory.Arithmetic {
  @NotNull
  private static NumberLiteral literal(@NotNull final Expression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1.");
    return $;
  }

  @NotNull
  private static InfixExpression replacement(@NotNull final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }

  @Nullable
  @Override public String description(final CastExpression ¢) {
    return "Use 1.*" + expression(¢) + " instead of (double)" + expression(¢);
  }

  @Override public ASTNode replacement(final CastExpression ¢) {
    return eval(() -> replacement(step.expression(¢))).when(step.type(¢).isPrimitiveType() && "double".equals(step.type(¢) + ""));
  }
}
