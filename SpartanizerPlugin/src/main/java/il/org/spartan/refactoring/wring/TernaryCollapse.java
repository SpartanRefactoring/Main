package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.ast.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** A {@link Wring} to simplify a conditional expression containing another
 * conditional expression, when two of the three inner expressions are
 * identical, e.g., converting
 *
 * <pre>
 * a ? b ? x : z : z
 * </pre>
 *
 * into
 *
 * <pre>
 * a &amp;&amp; b ? x : z
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-9-19 */
public final class TernaryCollapse extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.DistributiveRefactoring {
  private static Expression collapse(final ConditionalExpression e) {
    if (e == null)
      return null;
    Expression $;
    return ($ = collapseOnElse(e)) != null || ($ = collaspeOnThen(e)) != null ? $ : null;
  }

  private static Expression collapseOnElse(final ConditionalExpression e) {
    final ConditionalExpression elze = az.conditionalExpression(core(e.getElseExpression()));
    if (elze == null)
      return null;
    final Expression then = core(e.getThenExpression());
    final Expression elseThen = core(elze.getThenExpression());
    final Expression elseElse = core(elze.getElseExpression());
    return !wizard.same(then, elseElse) && !wizard.same(then, elseThen) ? null
        : wizard.same(then, elseElse)
            ? subject.pair(elseThen, then).toCondition(subject.pair(make.notOf(e.getExpression()), elze.getExpression()).to(CONDITIONAL_AND))
            : subject.pair(elseElse, then)
                .toCondition(subject.pair(make.notOf(e.getExpression()), make.notOf(elze.getExpression())).to(CONDITIONAL_AND));
  }

  private static Expression collaspeOnThen(final ConditionalExpression e) {
    final ConditionalExpression then = az.conditionalExpression(core(e.getThenExpression()));
    if (then == null)
      return null;
    final Expression elze = core(e.getElseExpression());
    final Expression thenThen = core(then.getThenExpression());
    final Expression thenElse = core(then.getElseExpression());
    return wizard.same(thenElse, elze)
        ? subject.pair(thenThen, elze).toCondition(subject.pair(e.getExpression(), then.getExpression()).to(CONDITIONAL_AND))
        : wizard.same(thenThen, elze)
            ? subject.pair(thenElse, elze).toCondition(subject.pair(e.getExpression(), make.notOf(then.getExpression())).to(CONDITIONAL_AND)) : null;
  }

  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }

  @Override Expression replacement(final ConditionalExpression e) {
    return collapse(e);
  }

  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return collapse(e) != null;
  }
}
