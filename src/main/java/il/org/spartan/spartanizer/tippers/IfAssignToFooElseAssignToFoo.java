package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code
 * if (x)
 *   a += 3;
 * else
 *   a += 9;
 * } into {@code
 * a += x ? 3 : 9;
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-29 */
public final class IfAssignToFooElseAssignToFoo extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = -2214694723277558846L;

  @Nullable @Override public String description(final IfStatement ¢) {
    return "Consolidate assignments to " + to(extract.assignment(then(¢)));
  }

  @Override public Statement replacement(@NotNull final IfStatement s) {
    @Nullable final Assignment $ = extract.assignment(then(s)), elze = extract.assignment(elze(s));
    return !wizard.compatible($, elze) ? null
        : subject.pair(to($), subject.pair(from($), from(elze)).toCondition(s.getExpression())).toStatement($.getOperator());
  }
}
