package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** Removes unnecessary uses of Boolean.valueOf, e.g.,
 * <code>Boolean.valueOf(true) </code> into <code>Boolean.TRUE</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class MethodInvocationValueOfBooleanConstant extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Collapse {
  private static String asString(final BooleanLiteral l) {
    return l.booleanValue() ? "TRUE" : "FALSE";
  }

  private static Expression replacement(final Expression x, final BooleanLiteral l) {
    return l == null ? null : subject.operand(x).toQualifier(asString(l));
  }

  private static Expression replacement(final Expression x, final Expression $) {
    return x == null || !"Boolean".equals(x + "") ? null : replacement(x, az.booleanLiteral($));
  }

  @Override String description(final MethodInvocation i) {
    return "Replace valueOf (" + onlyArgument(i) + ") with Boolean." + asString(az.booleanLiteral(onlyArgument(i)));
  }

  @Override Expression replacement(final MethodInvocation i) {
    return !"valueOf".equals(step.name(i).getIdentifier()) ? null : replacement(step.receiver(i), onlyArgument(i));
  }
}
