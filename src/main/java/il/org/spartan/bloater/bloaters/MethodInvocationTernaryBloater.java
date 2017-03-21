package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0984} Issue #984 convert {@code o.f(x ? a : b); }
 * to {@code if (x) o.f(a); else o.f(b); }
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryBloater extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -373710981362225466L;

  @Override public ASTNode replacement(final ExpressionStatement s) {
    final MethodInvocation i = az.methodInvocation(s.getExpression());
    if (i == null)
      return null;
    final ConditionalExpression $ = findFirst.conditionalArgument(i);
    if ($ == null)
      return null;
    // TODO Tomer Dragucki : use class subject --yg
    final MethodInvocation mThen = copy.of(i);
    final int ci = mThen.arguments().indexOf(findFirst.conditionalArgument(mThen));
    arguments(mThen).set(ci, copy.of(then($)));
    final MethodInvocation mElse = copy.of(i);
    arguments(mElse).set(ci, copy.of(elze($)));
    return subject.pair(subject.operand(mThen).toStatement(), subject.operand(mElse).toStatement()).toIf(copy.of($.getExpression()));
  }

  @Override @SuppressWarnings("unused") public String description(final ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }
}
