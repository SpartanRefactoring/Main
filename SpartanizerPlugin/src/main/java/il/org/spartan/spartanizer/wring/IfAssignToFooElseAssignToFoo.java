package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
  @Override String description(final IfStatement ¢) {
    return "Consolidate assignments to " + left(extract.assignment(then(¢)));
  }

  @Override Statement replacement(final IfStatement s) {
    final Assignment then = extract.assignment(then(s));
    final Assignment elze = extract.assignment(elze(s));
    return !wizard.compatible(then, elze) ? null
        : subject.pair(left(then), subject.pair(right(then), right(elze)).toCondition(s.getExpression())).toStatement(then.getOperator());
  }

  @Override boolean scopeIncludes(final IfStatement ¢) {
    return ¢ != null && wizard.compatible(extract.assignment(then(¢)), extract.assignment(elze(¢)));
  }
}
