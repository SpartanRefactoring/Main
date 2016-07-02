package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to simplify a conditional expression containing another
 * conditional expression, when two of the three inner expressions are
 * identical, e.g., converting <code>a ? b ? x : z :z</code> into
 * <code>a && b ? x : z</code>.
 *
 * @author Yossi Gil
 * @since 2015-9-19
 */
public class TernaryCollapse extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarize {
  private static Expression collapse(final ConditionalExpression e) {
    if (e == null)
      return null;
    Expression $;
    return ($ = collapseOnElse(e)) != null || ($ = collaspeOnThen(e)) != null ? $ : null;
  }
  private static Expression collapseOnElse(final ConditionalExpression e) {
    final ConditionalExpression elze = asConditionalExpression(core(e.getElseExpression()));
    if (elze == null)
      return null;
    final Expression then = core(e.getThenExpression());
    final Expression elseThen = core(elze.getThenExpression());
    final Expression elseElse = core(elze.getElseExpression());
    return !same(then, elseElse) && !same(then, elseThen) ? null : same(then, elseElse) ? Subject.pair(elseThen, then).toCondition(
        Subject.pair(logicalNot(e.getExpression()), elze.getExpression()).to(CONDITIONAL_AND)) : Subject.pair(elseElse, then)
        .toCondition(Subject.pair(logicalNot(e.getExpression()), logicalNot(elze.getExpression())).to(CONDITIONAL_AND));
  }
  private static Expression collaspeOnThen(final ConditionalExpression e) {
    final ConditionalExpression then = asConditionalExpression(core(e.getThenExpression()));
    if (then == null)
      return null;
    final Expression elze = core(e.getElseExpression());
    final Expression thenThen = core(then.getThenExpression());
    final Expression thenElse = core(then.getElseExpression());
    return same(thenElse, elze) ? Subject.pair(thenThen, elze).toCondition(
        Subject.pair(e.getExpression(), then.getExpression()).to(CONDITIONAL_AND)) : same(thenThen, elze) ? Subject.pair(thenElse,
        elze).toCondition(Subject.pair(e.getExpression(), logicalNot(then.getExpression())).to(CONDITIONAL_AND)) : null;
  }
  @Override Expression replacement(final ConditionalExpression e) {
    return collapse(e);
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return collapse(e) != null;
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate nested conditional expression";
  }
}