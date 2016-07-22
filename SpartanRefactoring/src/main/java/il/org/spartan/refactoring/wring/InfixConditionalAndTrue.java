package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} to convert <code>b && true</code> to <code>b</code>
 *
 * @author Yossi Gil
 * @since 2015-07-20
 */
public final class InfixConditionalAndTrue extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Simplify {
  @Override Expression replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, true);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return Is.conditionalAnd(e) && have.trueLiteral(extract.allOperands(e));
  }
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }
  @Override public WringGroup kind() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override public void go(final ASTRewrite r, final TextEditGroup g) {
    // TODO Auto-generated method stub
  }
}