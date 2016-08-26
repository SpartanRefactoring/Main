package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code> s.equals("s")</code> by <code>"s".equals(s)</code>
 * @author Ori Roth
 * @since 2016/05/08 */
public final class StringEqualsConstant extends ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  final static List<String> mns = as.list("equals", "equalsIgnoreCase");

  @Override String description(final MethodInvocation i) {
    return "Write " + first(arguments(i)) + "." + name(i) + "(" + receiver(i) + ") instead of " + i;
  }

  /* (non-Javadoc)
   *
   * @see
   * il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode#replacement(org.
   * eclipse.jdt.core.dom.ASTNode) */
  @Override ASTNode replacement(final MethodInvocation i) {
    final SimpleName n = name(i);
    if (!mns.contains(n.toString()))
      return null;
    final Expression ¢ = onlyOne(arguments(i));
    if (¢ == null || !(¢ instanceof StringLiteral))
      return null;
    final Expression e = receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement(n, ¢, e);
  }

  private static ASTNode replacement(final SimpleName n, final Expression ¢, final Expression e) {
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(duplicate(¢));
    $.setName(duplicate(n));
    arguments($).add(duplicate(e));
    return $;
  }
}
