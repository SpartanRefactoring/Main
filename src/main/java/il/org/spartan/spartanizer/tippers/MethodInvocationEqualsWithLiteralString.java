package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code> s.equals("s")</code> by <code>"s".equals(s)</code>
 * @author Ori Roth
 * @since 2016/05/08 */
public final class MethodInvocationEqualsWithLiteralString extends ReplaceCurrentNode<MethodInvocation> implements TipperCategory.Idiomatic {
  static final List<String> mns = as.list("equals", "equalsIgnoreCase");

  private static ASTNode replacement(final SimpleName n, final Expression ¢, final Expression x) {
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(copy.of(¢));
    $.setName(copy.of(n));
    arguments($).add(copy.of(x));
    return $;
  }

  @Override public String description(final MethodInvocation ¢) {
    return "Write " + first(arguments(¢)) + "." + step.name(¢) + "(" + receiver(¢) + ") instead of " + ¢;
  }

  @Override public ASTNode replacement(final MethodInvocation i) {
    final SimpleName $ = name(i);
    if (!mns.contains($ + ""))
      return null;
    final Expression ¢ = onlyOne(arguments(i));
    if (¢ == null || !(¢ instanceof StringLiteral))
      return null;
    final Expression e = receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement($, ¢, e);
  }
}
