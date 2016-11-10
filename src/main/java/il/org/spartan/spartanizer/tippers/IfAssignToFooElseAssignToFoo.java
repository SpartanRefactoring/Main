package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

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
public final class IfAssignToFooElseAssignToFoo extends ReplaceCurrentNode<IfStatement> implements TipperCategory.Ternarization {
  @Override public String description(final IfStatement ¢) {
    return "Consolidate assignments to " + to(extract.assignment(then(¢)));
  }
  @Override public Statement replacement(final IfStatement s) {
    final Assignment then = extract.assignment(then(s));
    final Assignment elze = extract.assignment(elze(s));
    return !wizard.compatible(then, elze) ? null
        : subject.pair(to(then), subject.pair(from(then), from(elze)).toCondition(s.getExpression())).toStatement(then.getOperator());
  }
}
