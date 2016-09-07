package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.assemble.plant.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** A {@link Wring} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.NoImpact {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override Expression replacement(final ConditionalExpression e) {
    return plant(extract.core(e.getThenExpression())).into(e.getParent());
  }

  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return e != null && wizard.same(e.getThenExpression(), e.getElseExpression());
  }
}
