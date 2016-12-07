package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.make.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends ReplaceCurrentNode<ConditionalExpression> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override public boolean prerequisite(final ConditionalExpression ¢) {
    return ¢ != null && wizard.same(¢.getThenExpression(), ¢.getElseExpression()) && sideEffects.free(¢.getExpression());
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return plant(extract.core(¢.getThenExpression())).into(¢.getParent());
  }
}
