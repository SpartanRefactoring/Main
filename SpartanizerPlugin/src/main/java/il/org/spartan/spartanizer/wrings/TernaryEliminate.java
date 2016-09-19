package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.assemble.make.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** A {@link Wring} to eliminate a ternary in which both branches are identical
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class TernaryEliminate extends ReplaceCurrentNode<ConditionalExpression> implements Kind.NOP {
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }

  @Override public boolean prerequisite(final ConditionalExpression ¢) {
    return ¢ != null && wizard.same(¢.getThenExpression(), ¢.getElseExpression());
  }

  @Override public Expression replacement(final ConditionalExpression ¢) {
    return plant(extract.core(¢.getThenExpression())).into(¢.getParent());
  }
}
