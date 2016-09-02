package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.assemble.Plant.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;

/** A {@link Wring} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.NoImpact {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override Expression replacement(final ConditionalExpression x) {
    return plant(extract.core(x.getThenExpression())).into(x.getParent());
  }

  @Override boolean scopeIncludes(final ConditionalExpression x) {
    return x != null && wizard.same(x.getThenExpression(), x.getElseExpression());
  }
}
