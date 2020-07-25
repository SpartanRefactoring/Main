package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static il.org.spartan.spartanizer.tippers.TernaryPushdown.pushdown;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code
 * if (x)
 *   f(a);
 * else
 *   f(b);
 * } into {@code
 * f(x ? a : b);
 * }
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfExpressionStatementElseSimilarExpressionStatement extends ReplaceCurrentNode<IfStatement> //
    implements Category.Ternarization {
  private static final long serialVersionUID = 0x5FEBB8952E19E3B1L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate two branches of 'if' into a ternary exrpession";
  }
  @Override public Statement replacement(final IfStatement s) {
    final Expression then = expression(extract.expressionStatement(then(s)));
    if (then == null)
      return null;
    final Expression elze = expression(extract.expressionStatement(elze(s)));
    if (elze == null)
      return null;
    final Expression $ = pushdown(subject.pair(then, elze).toCondition(s.getExpression()));
    return $ == null ? null : subject.operand($).toStatement();
  }
}
