package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.refactoring.wring.TernaryPushdown.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (x) f(a); else f(b);</code> into
 * <code>f(x ? a : b);</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfExpressionStatementElseSimilarExpressionStatement extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    final Expression then = expression(expressionStatement(then(s)));
    if (then == null)
      return null;
    final Expression elze = expression(expressionStatement(elze(s)));
    if (elze == null)
      return null;
    final Expression e = pushdown(Subject.pair(then, elze).toCondition(s.getExpression()));
    return e == null ? null : Subject.operand(e).toStatement();
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Consolidate two branches of an 'if' into a single ";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}