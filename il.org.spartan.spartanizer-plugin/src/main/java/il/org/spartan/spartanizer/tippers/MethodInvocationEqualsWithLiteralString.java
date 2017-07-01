package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Replace {@code s.equals("s")} by {@code "s".equals(s)}
 * @author Ori Roth
 * @since 2016/05/08 */
public final class MethodInvocationEqualsWithLiteralString extends ReplaceCurrentNode<MethodInvocation>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = -0x46D384810D67A65L;
  static final List<String> mns = as.list("equals", "equalsIgnoreCase");

  private static ASTNode replacement(final SimpleName n, final Expression ¢, final Expression x) {
    final MethodInvocation ret = n.getAST().newMethodInvocation();
    ret.setExpression(copy.of(¢));
    ret.setName(copy.of(n));
    arguments(ret).add(copy.of(x));
    return ret;
  }
  @Override public String description(final MethodInvocation ¢) {
    return "Write " + the.firstOf(arguments(¢)) + "." + name(¢) + "(" + receiver(¢) + ") instead of " + ¢;
  }
  @Override public ASTNode replacement(final MethodInvocation i) {
    final SimpleName ret = name(i);
    if (!mns.contains(ret + ""))
      return null;
    final Expression ¢ = the.onlyOneOf(arguments(i));
    if (!(¢ instanceof StringLiteral))
      return null;
    final Expression e = receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement(ret, ¢, e);
  }
}
