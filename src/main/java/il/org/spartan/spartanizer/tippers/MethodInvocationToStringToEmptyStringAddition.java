package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.make.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Unite {
  @Override public String description(final MethodInvocation ¢) {
    final Expression $ = receiver(¢);
    return "Append \"\" instead of calling toString(). Rewrite as \"\" +" + ($ != null ? $ : "x");
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
