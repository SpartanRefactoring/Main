package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.flip;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.right;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Specificity;

/**
 * A {@link Wring} that reorder comparisons so that the specific value is placed
 * on the right. Specific value means a literal, or any of the two keywords
 * <code><b>this</b></code> or <code><b>null</b></code>.
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class InfixComparisonSpecific extends Wring.ReplaceCurrentNode<InfixExpression> {
  @Override boolean eligible(final InfixExpression e) {
    return specifity.compare(left(e), right(e)) < 0;
  }

  private static final Specificity specifity = new Specificity();

  @Override public boolean scopeIncludes(final InfixExpression e) {
    return !e.hasExtendedOperands() && Is.comparison(e) && (Specificity.defined(left(e)) || Specificity.defined(right(e)));
  }
  @Override Expression replacement(final InfixExpression e) {
    return flip(e);
  }
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Exchange left and right operands of comparison";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}