package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TernaryPushdown.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

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
    implements TipperCategory.Ternarization {
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
