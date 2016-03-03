package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.elze;
import static org.spartan.refactoring.utils.Funcs.then;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (x)
 *   return b;
 * else
 *   return c;</code> into <code>return  x? b : c</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfReturnFooElseReturnBar extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    final Expression condition = s.getExpression();
    final Expression then = Extract.returnExpression(then(s));
    final Expression elze = Extract.returnExpression(elze(s));
    return then == null || elze == null ? null : Subject.operand(Subject.pair(then, elze).toCondition(condition)).toReturn();
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && Extract.returnExpression(then(s)) != null && Extract.returnExpression(elze(s)) != null;
  }
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Replace if with a return of a conditional statement";
  }
}