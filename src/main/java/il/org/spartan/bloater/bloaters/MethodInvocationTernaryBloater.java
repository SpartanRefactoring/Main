package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0984} Issue #984 convert {@code o.f(x ? a : b); }
 * to <code> if (x) o.f(a); else o.f(b); </code>
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryBloater extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Bloater {
  @Override public ASTNode replacement(final ExpressionStatement s) {
    final MethodInvocation i = az.methodInvocation(s.getExpression());
    if (i == null)
      return null;
    final ConditionalExpression $ = findFirst.conditionalArgument(i);
    if ($ == null)
      return null;
    final MethodInvocation mThen = copy.of(i);
    final int ci = mThen.arguments().indexOf(findFirst.conditionalArgument(mThen));
    step.arguments(mThen).set(ci, copy.of($.getThenExpression()));
    final MethodInvocation mElse = copy.of(i);
    step.arguments(mElse).set(ci, copy.of($.getElseExpression()));
    return subject.pair(subject.operand(mThen).toStatement(), subject.operand(mElse).toStatement()).toIf(copy.of($.getExpression()));
  }

  @Override @SuppressWarnings("unused") public String description(final ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }
}
