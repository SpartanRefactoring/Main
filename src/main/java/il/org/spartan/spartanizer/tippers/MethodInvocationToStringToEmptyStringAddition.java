package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = 6764541766975586375L;

  @NotNull @Override public String description(final MethodInvocation ¢) {
    @NotNull final Expression $ = receiver(¢);
    return "Prepend \"\" instead of calling toString(). Rewrite as \"\" +" + ($ != null ? $ : "x");
  }

  @Override public ASTNode replacement(@NotNull final MethodInvocation i) {
    if (!"toString".equals(name(i).getIdentifier()) || !arguments(i).isEmpty() || iz.expressionStatement(parent(i)))
      return null;
    @NotNull final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    final InfixExpression $ = subject.pair(make.makeEmptyString(i), receiver).to(PLUS2);
    return !iz.methodInvocation(parent(i)) ? $ : make.parethesized($);
  }
}
