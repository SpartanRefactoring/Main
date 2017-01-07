package il.org.spartan.zoomer.inflate.zoomers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link MethodInvocationTernaryExpanderTest} Issue No. 984
 * convert <code> o.f(x ? a : b); </code> to
 * <code> if (x) o.f(a); else o.f(b); </code>
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryExpander extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.Expander {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final ExpressionStatement s) {
    final Expression e = s.getExpression();
    if (!(e instanceof MethodInvocation))
      return null;
    final MethodInvocation i = (MethodInvocation) e;
    final ConditionalExpression c = getFirstCond(i);
    if (c == null)
      return null;
    final IfStatement $ = i.getAST().newIfStatement();
    $.setExpression(duplicate.of(c.getExpression()));
    final MethodInvocation mThen = duplicate.of(i);
    final int ci = mThen.arguments().indexOf(getFirstCond(mThen));
    ((List<Expression>) mThen.arguments()).set(ci, duplicate.of(c.getThenExpression()));
    $.setThenStatement(s.getAST().newExpressionStatement(mThen));
    final MethodInvocation mElse = duplicate.of(i);
    ((List<Expression>) mElse.arguments()).set(ci, duplicate.of(c.getElseExpression()));
    $.setElseStatement(s.getAST().newExpressionStatement(mElse));
    return $;
  }

  @Override @SuppressWarnings("unused") public String description(final ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }

  @SuppressWarnings("unchecked") private static ConditionalExpression getFirstCond(final MethodInvocation ¢) {
    for (final Expression $ : (List<Expression>) ¢.arguments())
      if ($ instanceof ConditionalExpression)
        return (ConditionalExpression) $;
    return null;
  }
}
