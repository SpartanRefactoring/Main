package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** convert
 *
 * <pre>
 * if (x)
 *   a += 3;
 * else
 *   a += 9;
 * </pre>
 *
 * into
 *
 * <pre>
 * a += x ? 3 : 9;
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class IfAssignToFooElseAssignToFoo extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Ternarization {
  @Override String description(final IfStatement s) {
    return "Consolidate assignments to " + step.left(extract.assignment(step.then(s)));
  }

  @Override Statement replacement(final IfStatement s) {
    final Assignment then = extract.assignment(step.then(s));
    final Assignment elze = extract.assignment(step.elze(s));
    return !wizard.compatible(then, elze) ? null
        : subject.pair(step.left(then), subject.pair(step.right(then), step.right(elze)).toCondition(s.getExpression()))
            .toStatement(then.getOperator());
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && wizard.compatible(extract.assignment(step.then(s)), extract.assignment(step.elze(s)));
  }
}
