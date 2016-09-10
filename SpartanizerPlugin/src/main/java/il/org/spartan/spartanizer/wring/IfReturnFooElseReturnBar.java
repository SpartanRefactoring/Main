package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
    final Expression then = extract.returnExpression(step.then(s));
    final Expression elze = extract.returnExpression(step.elze(s));
    return then == null || elze == null ? null : subject.operand(subject.pair(then, elze).toCondition(condition)).toReturn();
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && extract.returnExpression(step.then(s)) != null && extract.returnExpression(step.elze(s)) != null;
  }
}
