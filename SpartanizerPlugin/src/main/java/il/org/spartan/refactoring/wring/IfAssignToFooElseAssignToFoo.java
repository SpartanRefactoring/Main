package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

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
    return "Consolidate assignments to " + navigate.left(extract.assignment(navigate.then(s)));
  }

  @Override Statement replacement(final IfStatement s) {
    final Assignment then = extract.assignment(navigate.then(s));
    final Assignment elze = extract.assignment(navigate.elze(s));
    return !wizard.compatible(then, elze) ? null
        : subject.pair(navigate.left(then), subject.pair(navigate.right(then), navigate.right(elze)).toCondition(s.getExpression()))
            .toStatement(then.getOperator());
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && wizard.compatible(extract.assignment(navigate.then(s)), extract.assignment(navigate.elze(s)));
  }
}
