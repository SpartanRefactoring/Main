package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;


/** Replaces, e.g., {@code Integer x=new Integer(2);} with
 * {@code Integer x=Integer.valueOf(2);}, more generally new of of any boxed
 * primitive types/{@link String} with recommended factory method
 * {@code valueOf()}
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-06 */
public final class ClassInstanceCreationValueTypes extends ReplaceCurrentNode<ClassInstanceCreation>//
    implements TipperCategory.Idiomatic {
  @Override  public String description( final ClassInstanceCreation ¢) {
    return "Use factory method " + hop.simpleName(¢.getType()) + ".valueOf() instead of new ";
  }

  @Override public ASTNode replacement( final ClassInstanceCreation c) {
    final Expression e = onlyOne(arguments(c));
    if (e == null)
      return null;
    final Type t = c.getType();
    if (!wizard.isValueType(t))
      return null;
    final SimpleName simpleName = hop.simpleName(t);
    final MethodInvocation $ = subject.operand(copy.of(simpleName)).toMethod("valueOf");
    arguments($).add(copy.of(e));
    return $;
  }
}