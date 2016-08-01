package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to eliminate a ternary in which both branches are identical
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class TernaryEliminate extends Wring.ReplaceCurrentNode<ConditionalExpression> {
  @Override Expression replacement(final ConditionalExpression e) {
    return new Plant(extract.core(e.getThenExpression())).into(e.getParent());
  }
  @Override boolean scopeIncludes(final ConditionalExpression e) {
    return e != null && same(e.getThenExpression(), e.getElseExpression());
  }
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Eliminate conditional exprssion with identical branches";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}