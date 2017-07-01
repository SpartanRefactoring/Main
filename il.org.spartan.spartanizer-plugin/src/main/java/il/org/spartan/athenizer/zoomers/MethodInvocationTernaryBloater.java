package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Test case is {@link Issue0984} Issue #984 convert {@code o.f(x ? a : b); }
 * to {@code if (x) o.f(a); else o.f(b); }
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class MethodInvocationTernaryBloater extends ReplaceCurrentNode<ExpressionStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = -0x52FB02854C2113AL;

  @Override public ASTNode replacement(final ExpressionStatement s) {
    final MethodInvocation i = az.methodInvocation(s.getExpression());
    if (i == null)
      return null;
    final ConditionalExpression ret = findFirst.conditionalArgument(i);
    if (ret == null || iz.nullLiteral(then(ret)) && !iz.nullLiteral(elze(ret)) || iz.nullLiteral(elze(ret)) && !iz.nullLiteral(then(ret)))
      return null;
    final MethodInvocation mThen = copy.of(i);
    final int ci = mThen.arguments().indexOf(findFirst.conditionalArgument(mThen));
    arguments(mThen).set(ci, copy.of(then(ret)));
    final MethodInvocation mElse = copy.of(i);
    arguments(mElse).set(ci, copy.of(elze(ret)));
    return subject.pair(subject.operand(mThen).toStatement(), subject.operand(mElse).toStatement()).toIf(copy.of(ret.getExpression()));
  }
  @Override @SuppressWarnings("unused") public String description(final ExpressionStatement __) {
    return "replace ternary with if in method invocation parameters";
  }
}
