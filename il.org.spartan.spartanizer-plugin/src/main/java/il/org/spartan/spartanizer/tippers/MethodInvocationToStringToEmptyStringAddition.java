package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Transforms x.toString() to "" + x
 * @author Stav Namir
 * @author Niv Shalmon
 * @since 2016-8-31 */
public final class MethodInvocationToStringToEmptyStringAddition extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Strings {
  private static final long serialVersionUID = 0x5DE07AE5A0DF8047L;

  @Override public String description(final MethodInvocation ¢) {
    final Expression $ = receiver(¢);
    return "Prepend \"\" instead of calling toString(). Rewrite as \"\" +" + ($ != null ? $ : "x");
  }
  @Override public ASTNode replacement(final MethodInvocation i) {
    if (!"toString".equals(name(i).getIdentifier()) || !arguments(i).isEmpty() || iz.expressionStatement(parent(i)))
      return null;
    final Expression receiver = receiver(i);
    if (receiver == null)
      return null;
    final InfixExpression $ = subject.pair(make.emptyString(i), receiver).to(il.org.spartan.spartanizer.ast.navigate.op.PLUS2);
    return !iz.methodInvocation(parent(i)) ? $ : make.parethesized($);
  }
}
