package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code s.equals("s")} by {@code "s".equals(s)}
 * @author Ori Roth
 * @since 2016/05/08 */
public final class MethodInvocationEqualsWithLiteralString extends ReplaceCurrentNode<MethodInvocation>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -318973030784465509L;
  static final List<String> mns = as.list("equals", "equalsIgnoreCase");

  private static ASTNode replacement(@NotNull final SimpleName n, final Expression ¢, final Expression x) {
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(copy.of(¢));
    $.setName(copy.of(n));
    arguments($).add(copy.of(x));
    return $;
  }

  @Override @NotNull public String description(final MethodInvocation ¢) {
    return "Write " + first(arguments(¢)) + "." + name(¢) + "(" + receiver(¢) + ") instead of " + ¢;
  }

  @Override public ASTNode replacement(final MethodInvocation i) {
    @NotNull final SimpleName $ = name(i);
    if (!mns.contains($ + ""))
      return null;
    final Expression ¢ = onlyOne(arguments(i));
    if (!(¢ instanceof StringLiteral))
      return null;
    @NotNull final Expression e = receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement($, ¢, e);
  }
}
