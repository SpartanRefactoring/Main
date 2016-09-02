package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.ast.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** Removes unnecessary uses of Boolean.valueOf, e.g.,
 * <code>Boolean.valueOf(true) </code> into <code>Boolean.TRUE</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override String description(final MethodInvocation i) {
    return "Replace valueOf (" + onlyArgument(i) + ") with Boolean." + asString(az.booleanLiteral(onlyArgument(i)));
  }

  @Override Expression replacement(final MethodInvocation i) {
    return !"valueOf".equals(step.name(i).getIdentifier()) ? null : replacement(step.receiver(i), onlyArgument(i));
  }

  private static Expression replacement(final Expression e, final Expression $) {
    return e == null || !"Boolean".equals("" + e) ? null : replacement(e, az.booleanLiteral($));
  }

  private static Expression replacement(final Expression e, final BooleanLiteral l) {
    return l == null ? null : subject.operand(e).toQualifier(asString(l));
  }

  private static String asString(final BooleanLiteral l) {
    return (l.booleanValue() ? "TRU" : "FALS") + "E";
  }
}
