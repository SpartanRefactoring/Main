package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

/**
 * A {@link Wring} that reorder comparisons so that the specific value is placed
 * on the right. Specific value means a literal, or any of the two keywords
 * <code><b>this</b></code> or <code><b>null</b></code>.
 *
 * @author Yossi Gil
 * @since 2015-07-17
 */
public final class InfixComparisonSpecific extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.ReorganizeExpression {
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
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}