package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.assemble.make.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.wizard.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends ReplaceCurrentNode<MethodInvocation> implements Kind.Collapse {
  @Override public String description(final MethodInvocation i) {
    final Expression receiver = receiver(i);
    return "Append \"\" instead of calling toString(). Rewrite as \"\" +" + (receiver == null ? "x" : receiver);
  }

  @Override public ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(step.name(i).getIdentifier()) || !arguments(i).isEmpty() || iz.expressionStatement(parent(i)))
      return null;
    final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    final InfixExpression $ = subject.pair(makeEmptyString(i), receiver).to(PLUS2);
    return !iz.methodInvocation(parent(i)) ? $ : parethesized($);
  }
}
