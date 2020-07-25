package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import il.org.spartan.spartanizer.ast.factory.cons;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Converts {@code a?b?x:z:z}into {@code a&&b?x:z}
 * @author Yossi Gil
 * @since 2015-9-19 */
public final class TernaryCollapse extends ReplaceCurrentNode<ConditionalExpression>//
    implements Category.Theory.Logical {
  private static final long serialVersionUID = -0x76A72FCF26B6A18L;

  private static Expression collapse(final ConditionalExpression ¢) {
    Expression $;
    return ($ = collapseOnElse(¢)) != null || ($ = collaspeOnThen(¢)) != null ? $ : null;
  }
  private static Expression collapseOnElse(final ConditionalExpression x) {
    final ConditionalExpression $ = az.conditionalExpression(core(x.getElseExpression()));
    if ($ == null)
      return null;
    final Expression then = core(x.getThenExpression()), elseThen = core(then($)), elseElse = core(elze($));
    return !wizard.eq(then, elseElse) && !wizard.eq(then, elseThen) ? null
        : wizard.eq(then, elseElse)
            ? subject.pair(elseThen, then).toCondition(subject.pair(cons.not(x.getExpression()), $.getExpression()).to(CONDITIONAL_AND))
            : subject.pair(elseElse, then)
                .toCondition(subject.pair(cons.not(x.getExpression()), cons.not($.getExpression())).to(CONDITIONAL_AND));
  }
  private static Expression collaspeOnThen(final ConditionalExpression x) {
    final ConditionalExpression $ = az.conditionalExpression(core(x.getThenExpression()));
    if ($ == null)
      return null;
    final Expression elze = core(x.getElseExpression()), thenThen = core(then($)), thenElse = core(elze($));
    return wizard.eq(thenElse, elze)
        ? subject.pair(thenThen, elze).toCondition(subject.pair(x.getExpression(), $.getExpression()).to(CONDITIONAL_AND))
        : wizard.eq(thenThen, elze)
            ? subject.pair(thenElse, elze).toCondition(subject.pair(x.getExpression(), cons.not($.getExpression())).to(CONDITIONAL_AND)) : null;
  }
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }
  @Override public Expression replacement(final ConditionalExpression ¢) {
    return collapse(¢);
  }
}
