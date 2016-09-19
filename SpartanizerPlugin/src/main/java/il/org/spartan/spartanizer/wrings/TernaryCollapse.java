package il.org.spartan.spartanizer.wrings;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Converts <code>a?b?x:z:z</code>into <code>a&&b?x:z</code>
 * @author Yossi Gil
 * @since 2015-9-19 */
public final class TernaryCollapse extends ReplaceCurrentNode<ConditionalExpression> implements Kind.DistributiveRefactoring {
  private static Expression collapse(final ConditionalExpression ¢) {
    if (¢ == null)
      return null;
    Expression $;
    return ($ = collapseOnElse(¢)) != null || ($ = collaspeOnThen(¢)) != null ? $ : null;
  }

  private static Expression collapseOnElse(final ConditionalExpression x) {
    final ConditionalExpression elze = az.conditionalExpression(core(x.getElseExpression()));
    if (elze == null)
      return null;
    final Expression then = core(x.getThenExpression());
    final Expression elseThen = core(elze.getThenExpression());
    final Expression elseElse = core(elze.getElseExpression());
    return !wizard.same(then, elseElse) && !wizard.same(then, elseThen) ? null
        : wizard.same(then, elseElse)
            ? subject.pair(elseThen, then).toCondition(subject.pair(make.notOf(x.getExpression()), elze.getExpression()).to(CONDITIONAL_AND))
            : subject.pair(elseElse, then)
                .toCondition(subject.pair(make.notOf(x.getExpression()), make.notOf(elze.getExpression())).to(CONDITIONAL_AND));
  }

  private static Expression collaspeOnThen(final ConditionalExpression x) {
    final ConditionalExpression then = az.conditionalExpression(core(x.getThenExpression()));
    if (then == null)
      return null;
    final Expression elze = core(x.getElseExpression());
    final Expression thenThen = core(then.getThenExpression());
    final Expression thenElse = core(then.getElseExpression());
    return wizard.same(thenElse, elze)
        ? subject.pair(thenThen, elze).toCondition(subject.pair(x.getExpression(), then.getExpression()).to(CONDITIONAL_AND))
        : wizard.same(thenThen, elze)
            ? subject.pair(thenElse, elze).toCondition(subject.pair(x.getExpression(), make.notOf(then.getExpression())).to(CONDITIONAL_AND)) : null;
  }

  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return collapse(¢);
  }
}
