package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** convert
 *
 * <pre>
 * if (x)
 *   return b;
 * else
 *   return c;
 * </pre>
 *
 * into
 *
 * <pre>
 * return x? b : c
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfReturnFooElseReturnBar extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final IfStatement __) {
    return "Replace if with a return of a conditional statement";
  }

  @Override Statement replacement(final IfStatement s) {
    final Expression condition = s.getExpression();
    final Expression then = extract.returnExpression(navigate.then(s));
    final Expression elze = extract.returnExpression(navigate.elze(s));
    return then == null || elze == null ? null : subject.operand(subject.pair(then, elze).toCondition(condition)).toReturn();
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && extract.returnExpression(navigate.then(s)) != null && extract.returnExpression(navigate.elze(s)) != null;
  }
}
