package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removes unnecessary uses of Boolean.valueOf, e.g.,
 * {@code Boolean.valueOf(true) } into {@code Boolean.TRUE}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class MethodInvocationValueOfBooleanConstant extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0xC254837D2301241L;

  private static String asString(final BooleanLiteral ¢) {
    return ¢.booleanValue() ? "TRUE" : "FALSE";
  }

  private static Expression replacement(final Expression x, final BooleanLiteral l) {
    return l == null ? null : subject.operand(x).toQualifier(asString(l));
  }

  private static Expression replacement(final Expression x, final Expression $) {
    return x == null || !"Boolean".equals(x + "") ? null : replacement(x, az.booleanLiteral($));
  }

  @Override public String description(final MethodInvocation ¢) {
    return "Replace valueOf (" + onlyArgument(¢) + ") with Boolean." + asString(az.booleanLiteral(onlyArgument(¢)));
  }

  @Override public Expression replacement(final MethodInvocation ¢) {
    return iz.statement(¢.getParent()) || !"valueOf".equals(step.name(¢).getIdentifier()) ? null : replacement(step.receiver(¢), onlyArgument(¢));
  }
}
