package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.same;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Plant;

/**
 * A {@link Wring} to eliminate a ternary in which both branches are identical
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class TernaryEliminate extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  @Override Expression replacement(final ConditionalExpression e) {
    return new Plant(Extract.core(e.getThenExpression())).into(e.getParent());
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return e != null && same(e.getThenExpression(), e.getElseExpression());
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression _) {
    return "Eliminate conditional exprssion with identical branches";
  }
}