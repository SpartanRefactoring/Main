package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Replaces, e.g., <code>Integer x=new Integer(2);</code> with
 * <code>Integer x=Integer.valueOf(2);</code>, more generally new of of any
 * boxed primitive types/{@link String} with recommended factory method
 * <code>valueOf()</code>
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06 */
public final class ClassInstanceCreationValueTypes extends ReplaceCurrentNode<ClassInstanceCreation> implements Kind.SyntacticBaggage {
  @Override public String description(final ClassInstanceCreation ¢) {
    final Type t = ¢.getType();
    final SimpleName n = hop.simpleName(t);
    return "Use factory method " + n + ".valueOf() instead of new ";
  }

  @Override public ASTNode replacement(final ClassInstanceCreation c) {
    final Expression e = onlyOne(arguments(c));
    if (e == null)
      return null;
    final Type t = c.getType();
    if (!wizard.isValueType(t))
      return null;
    final SimpleName simpleName = hop.simpleName(t);
    if (simpleName == null)
      return null;
    final MethodInvocation $ = subject.operand(duplicate.of(simpleName)).toMethod("valueOf");
    arguments($).add(duplicate.of(e));
    return $;
  }
}