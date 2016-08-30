package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** Removes unnecessary uses of Boolean.valueOf, e.g.,
 * <code>Boolean.valueOf(true) </code> into <code>Boolean.TRUE</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override String description(final MethodInvocation i) {
    return "Replace valueOf (" + onlyArgument(i) + ") with Boolean." + asString(az.booleanLiteral(onlyArgument(i)));
  }

  @Override Expression replacement(final MethodInvocation i) {
    return !"valueOf".equals(navigate.name(i).getIdentifier()) ? null : replacement(navigate.receiver(i), onlyArgument(i));
  }

  private static Expression replacement(final Expression e, final Expression $) {
    return e == null || !"Boolean".equals(e.toString()) ? null : replacement(e, az.booleanLiteral($));
  }

  private static Expression replacement(final Expression e, final BooleanLiteral l) {
    return l == null ? null : subject.operand(e).toQualifier(asString(l));
  }

  private static String asString(final BooleanLiteral l) {
    return l.booleanValue() ? "TRUE" : "FALSE";
  }
}
