package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to remove unnecessary uses of Boolean.valueOf, for example
 * by converting <code>
 *
 * <pre>
 * Boolean b = Boolean.valueOf(true)
 * </pre>
 *
 * <code> into <code>
 *
 * <pre>
 * Boolean b = Boolean.TRUE
 * </pre>
 *
 * <code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-04 */
public final class BooleanConstants extends Wring.ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  @Override String description(@SuppressWarnings("unused") final MethodInvocation __) {
    return "Use built-in boolean constants instead of valueOf()";
  }

  @Override Expression replacement(final MethodInvocation i) {
    if (!"valueOf".equals(name(i).getIdentifier()))
      return null;
    final List<Expression> arguments = arguments(i);
    return arguments.size() != 1 ? null : replacement(expression(i), arguments.get(0));
  }

  private static Expression replacement(final Expression e, Expression $) {
    return e == null || !"Boolean".equals(e.toString()) ? null : replacement(e, asBooleanLiteral($));
  }

  private static Expression replacement(final Expression e, final BooleanLiteral l) {
    return l == null ? null : subject.operand(e).toQualifier(l.booleanValue() ? "TRUE" : "FALSE");
  }
}
