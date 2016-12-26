package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link MethodInvocationTernaryExpanderTest} Issue No. 984
 * convert <code> o.f(x ? a : b); </code> to
 * <code> if (x) o.f(a); else o.f(b); </code>
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryExpander extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.InVain {
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(ExpressionStatement s) {
    Expression e = s.getExpression();
    if (!(e instanceof MethodInvocation))
      return null;
    MethodInvocation i = (MethodInvocation) e;
    ConditionalExpression c = getFirstCond(i);
    if (c == null)
      return null;
    final IfStatement $ = i.getAST().newIfStatement();
    $.setExpression(duplicate.of(c.getExpression()));
    MethodInvocation mThen = duplicate.of(i);
    int ci = mThen.arguments().indexOf(getFirstCond(mThen));
    ((List<Expression>) mThen.arguments()).set(ci, duplicate.of(c.getThenExpression()));
    $.setThenStatement(s.getAST().newExpressionStatement(mThen));
    MethodInvocation mElse = duplicate.of(i);
    ((List<Expression>) mElse.arguments()).set(ci, duplicate.of(c.getElseExpression()));
    $.setElseStatement(s.getAST().newExpressionStatement(mElse));
    return $;
  }

  @Override @SuppressWarnings("unused") public String description(ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }

  @SuppressWarnings("unchecked") private static ConditionalExpression getFirstCond(MethodInvocation ¢) {
    for (Expression $ : (List<Expression>) ¢.arguments())
      if ($ instanceof ConditionalExpression)
        return (ConditionalExpression) $;
    return null;
  }
}
