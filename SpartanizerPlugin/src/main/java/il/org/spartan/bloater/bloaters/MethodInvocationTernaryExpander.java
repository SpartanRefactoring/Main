package il.org.spartan.bloater.bloaters;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0984} Issue #984 convert
 * <code> o.f(x ? a : b); </code> to <code> if (x) o.f(a); else o.f(b); </code>
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryExpander extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.Expander {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final ExpressionStatement s) {
    final Expression e = s.getExpression();
    if (!(e instanceof MethodInvocation))
      return null;
    // TODO: Tomer Dragucki rewrite your code using class 'az' and 'iz' --yg
    final MethodInvocation i = (MethodInvocation) e;
    final ConditionalExpression c = getFirstCond(i);
    if (c == null)
      return null;
    // TODO: omer Dragucki rewrite your code using subject.pairt()....
    final IfStatement $ = i.getAST().newIfStatement();
    $.setExpression(copy.of(c.getExpression()));
    final MethodInvocation mThen = copy.of(i);
    final int ci = mThen.arguments().indexOf(getFirstCond(mThen));
    ((List<Expression>) mThen.arguments()).set(ci, copy.of(c.getThenExpression()));
    $.setThenStatement(s.getAST().newExpressionStatement(mThen));
    final MethodInvocation mElse = copy.of(i);
    ((List<Expression>) mElse.arguments()).set(ci, copy.of(c.getElseExpression()));
    $.setElseStatement(s.getAST().newExpressionStatement(mElse));
    return $;
  }

  @Override @SuppressWarnings("unused") public String description(final ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }

  private static ConditionalExpression getFirstCond(final MethodInvocation ¢) {
    for (final Expression $ : step.arguments(¢))
      if ($ instanceof ConditionalExpression)
        return (ConditionalExpression) $;
    return null;
  }
}
