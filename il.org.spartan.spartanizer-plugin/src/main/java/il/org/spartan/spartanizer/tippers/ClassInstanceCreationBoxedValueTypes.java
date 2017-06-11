package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Replaces, e.g., {@code Integer x=new Integer(2);} with
 * {@code Integer x=Integer.valueOf(2);}, more generally new of of any boxed
 * primitive types/{@link String} with recommended factory method
 * {@code valueOf()}
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06 */
public final class ClassInstanceCreationBoxedValueTypes extends ReplaceCurrentNode<ClassInstanceCreation>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = 0x578B2D093DF1DBD5L;

  @Override public String description(final ClassInstanceCreation ¢) {
    return "Use factory method " + hop.simpleName(¢.getType()) + ".valueOf() instead of new ";
  }
  @Override public Examples examples() {
    return //
    convert("new Integer(x)")//
        .to("Integer.valueOf(x)")//
    ;
  }
  @Override public ASTNode replacement(final ClassInstanceCreation c) {
    final Expression e = the.onlyOneOf(arguments(c));
    if (e == null)
      return null;
    final Type t = c.getType();
    if (!type.isValueType(t))
      return null;
    final MethodInvocation $ = subject.operand(copy.of(hop.simpleName(t))).toMethod("valueOf");
    arguments($).add(copy.of(e));
    return $;
  }
}