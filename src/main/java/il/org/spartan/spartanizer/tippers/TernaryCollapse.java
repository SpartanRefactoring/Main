package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Converts <code>a?b?x:z:z</code>into <code>a&&b?x:z</code>
 * @author Yossi Gil
 * @since 2015-9-19 */
public final class TernaryCollapse extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.CommnonFactoring {
  private static Expression collapse(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    Expression $;
    return ($ = collapseOnElse(¢)) != null || ($ = collaspeOnThen(¢)) != null ? $ : null;
  }

  private static Expression collapseOnElse(final ConditionalExpression x) {
    final ConditionalExpression $ = az.conditionalExpression(core(x.getElseExpression()));
    if ($ == null)
      return null;
    final Expression then = core(x.getThenExpression()), elseThen = core($.getThenExpression()), elseElse = core($.getElseExpression());
    return !wizard.same(then, elseElse) && !wizard.same(then, elseThen) ? null
        : wizard.same(then, elseElse)
            ? subject.pair(elseThen, then).toCondition(subject.pair(make.notOf(x.getExpression()), $.getExpression()).to(CONDITIONAL_AND))
            : subject.pair(elseElse, then)
                .toCondition(subject.pair(make.notOf(x.getExpression()), make.notOf($.getExpression())).to(CONDITIONAL_AND));
  }

  private static Expression collaspeOnThen(final ConditionalExpression x) {
    final ConditionalExpression $ = az.conditionalExpression(core(x.getThenExpression()));
    if ($ == null)
      return null;
    final Expression elze = core(x.getElseExpression()), thenThen = core($.getThenExpression()), thenElse = core($.getElseExpression());
    return wizard.same(thenElse, elze)
        ? subject.pair(thenThen, elze).toCondition(subject.pair(x.getExpression(), $.getExpression()).to(CONDITIONAL_AND))
        : wizard.same(thenThen, elze)
            ? subject.pair(thenElse, elze).toCondition(subject.pair(x.getExpression(), make.notOf($.getExpression())).to(CONDITIONAL_AND)) : null;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return collapse(¢);
  }
}
