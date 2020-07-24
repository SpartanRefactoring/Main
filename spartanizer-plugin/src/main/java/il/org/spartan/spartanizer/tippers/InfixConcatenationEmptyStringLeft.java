package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import fluent.ly.lisp;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.issues.Issue0116;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Convert {@code ""+x} to {@code x+""} tests in {@link Issue0116}
 * @author Dan Greenstein
 * @author Niv Shalmon
 * @since 2016 */
public final class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Theory.Strings, Category.Transformation.Sort {
  private static final long serialVersionUID = -0xABED385B90F4612L;

  private static InfixExpression replace(final InfixExpression ¢) {
    final List<Expression> $ = extract.allOperands(¢);
    lisp.swap($, 0, 1);
    return subject.operands($).to(op.PLUS2);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Append, rather than prepend, \"\", to " + left(¢);
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.emptyStringLiteral(left(¢)) || !iz.infixPlus(¢) || iz.stringLiteral(right(¢)) ? null : replace(¢);
  }
}
