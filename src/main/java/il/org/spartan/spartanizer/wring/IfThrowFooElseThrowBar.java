package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** convert
 *
 * <pre>
 * if (x)
 *   throw b;
 * else
 *   throw c;
 * </pre>
 *
 * into
 *
 * <pre>
 * throw x? b : c
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfThrowFooElseThrowBar extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final IfStatement ____) {
    return "Consolidate 'if' into a 'throw' statement of a conditional expression";
  }

  @Override Statement replacement(final IfStatement s) {
    final Expression condition = s.getExpression();
    final Expression then = extract.throwExpression(step.then(s));
    final Expression elze = extract.throwExpression(step.elze(s));
    return then == null || elze == null ? null : make.throwOf(subject.pair(then, elze).toCondition(condition));
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && extract.throwExpression(step.then(s)) != null && extract.throwExpression(step.elze(s)) != null;
  }
}
