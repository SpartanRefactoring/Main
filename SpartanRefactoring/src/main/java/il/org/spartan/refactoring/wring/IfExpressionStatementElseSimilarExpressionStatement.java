package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import static il.org.spartan.refactoring.utils.extract.*;

import static il.org.spartan.refactoring.wring.TernaryPushdown.*;

/**
 * A {@link Wring} to convert <code>if (x) f(a); else f(b);</code> into
 * <code>f(x ? a : b);</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfExpressionStatementElseSimilarExpressionStatement extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.ConsolidateStatements {
  @Override Statement replacement(final IfStatement s) {
    final Expression then = extract.expression(extract.expressionStatement(then(s)));
    if (then == null)
      return null;
    final Expression elze = extract.expression(extract.expressionStatement(elze(s)));
    if (elze == null)
      return null;
    final Expression e = pushdown(Subject.pair(then, elze).toCondition(s.getExpression()));
    return e == null ? null : Subject.operand(e).toStatement();
  }
  @Override String description(final IfStatement s) {
    return "Consolidate two branches of 'if(" + s.getExpression() + ") ... into one";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}