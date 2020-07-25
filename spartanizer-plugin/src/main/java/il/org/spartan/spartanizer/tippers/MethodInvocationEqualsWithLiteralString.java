package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.receiver;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import fluent.ly.as;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code s.equals("s")} by {@code "s".equals(s)}
 * @author Ori Roth
 * @since 2016/05/08 */
public final class MethodInvocationEqualsWithLiteralString extends ReplaceCurrentNode<MethodInvocation>//
    implements Category.Idiomatic {
  private static final long serialVersionUID = -0x46D384810D67A65L;
  static final List<String> mns = as.list("equals", "equalsIgnoreCase");

  private static ASTNode replacement(final SimpleName n, final Expression ¢, final Expression x) {
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(copy.of(¢));
    $.setName(copy.of(n));
    arguments($).add(copy.of(x));
    return $;
  }
  @Override public String description(final MethodInvocation ¢) {
    return "Write " + the.firstOf(arguments(¢)) + "." + name(¢) + "(" + receiver(¢) + ") instead of " + ¢;
  }
  @Override public ASTNode replacement(final MethodInvocation i) {
    final SimpleName $ = name(i);
    if (!mns.contains($ + ""))
      return null;
    final Expression ¢ = the.onlyOneOf(arguments(i));
    if (!(¢ instanceof StringLiteral))
      return null;
    final Expression e = receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement($, ¢, e);
  }
}
