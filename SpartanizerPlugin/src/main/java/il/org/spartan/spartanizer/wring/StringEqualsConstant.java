package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code> s.equals("s")</code> by <code>"s".equals(s)</code>
 * @author Ori Roth
 * @since 2016/05/08 */
public final class StringEqualsConstant extends ReplaceCurrentNode<MethodInvocation> implements Kind.Canonicalization {
  static final List<String> mns = as.list("equals", "equalsIgnoreCase");

  private static ASTNode replacement(final SimpleName n, final Expression ¢, final Expression e) {
    final MethodInvocation $ = n.getAST().newMethodInvocation();
    $.setExpression(duplicate.of(¢));
    $.setName(duplicate.of(n));
    arguments($).add(duplicate.of(e));
    return $;
  }

  @Override String description(final MethodInvocation i) {
    return "Write " + lisp.first(arguments(i)) + "." + step.name(i) + "(" + step.receiver(i) + ") instead of " + i;
  }

  /* (non-Javadoc)
   *
   * @see
   * il.org.spartan.spartanizer.wring.Wring.ReplaceCurrentNode#replacement(org.
   * eclipse.jdt.core.dom.ASTNode) */
  @Override ASTNode replacement(final MethodInvocation i) {
    final SimpleName n = step.name(i);
    if (!mns.contains("" + n))
      return null;
    final Expression ¢ = lisp.onlyOne(arguments(i));
    if (¢ == null || !(¢ instanceof StringLiteral))
      return null;
    final Expression e = step.receiver(i);
    return e == null || e instanceof StringLiteral ? null : replacement(n, ¢, e);
  }
}
