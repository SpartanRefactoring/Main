package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

import il.org.spartan.athenizer.zoom.zoomers.Issue0984;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

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
    final ConditionalExpression $ = findFirst.conditionalArgument(i);
    if ($ == null || iz.nullLiteral(then($)) && !iz.nullLiteral(elze($)) || iz.nullLiteral(elze($)) && !iz.nullLiteral(then($)))
      return null;
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
