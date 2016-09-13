package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public final class IfAssignToFooElseAssignToFoo extends ReplaceCurrentNode<IfStatement> implements Kind.Ternarization {
  @Override public String description(final IfStatement ¢) {
    return "Consolidate assignments to " + to(extract.assignment(then(¢)));
  }

  @Override public Statement replacement(final IfStatement s) {
    final Assignment then = extract.assignment(then(s));
    final Assignment elze = extract.assignment(elze(s));
    return !wizard.compatible(then, elze) ? null
        : subject.pair(to(then), subject.pair(from(then), from(elze)).toCondition(s.getExpression())).toStatement(then.getOperator());
  }

  @Override public boolean claims(final IfStatement ¢) {
    return ¢ != null && wizard.compatible(extract.assignment(then(¢)), extract.assignment(elze(¢)));
  }
}
