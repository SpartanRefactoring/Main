package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Removes unnecessary uses of Boolean.valueOf, e.g.,
 * {@code Boolean.valueOf(true) } into {@code Boolean.TRUE}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class MethodInvocationValueOfBooleanConstant extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  @NotNull private static String asString(@NotNull final BooleanLiteral ¢) {
    return ¢.booleanValue() ? "TRUE" : "FALSE";
  }

  @Nullable private static Expression replacement(final Expression x, @Nullable final BooleanLiteral l) {
    return l == null ? null : subject.operand(x).toQualifier(asString(l));
  }

  @Nullable private static Expression replacement(@Nullable final Expression x, final Expression $) {
    return x == null || !"Boolean".equals(x + "") ? null : replacement(x, az.booleanLiteral($));
  }

  @Override @NotNull public String description(final MethodInvocation ¢) {
    return "Replace valueOf (" + onlyArgument(¢) + ") with Boolean." + asString(az.booleanLiteral(onlyArgument(¢)));
  }

  @Override public Expression replacement(final MethodInvocation ¢) {
    return !"valueOf".equals(step.name(¢).getIdentifier()) ? null : replacement(step.receiver(¢), onlyArgument(¢));
  }
}
