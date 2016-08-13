package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert
 *
 * <pre>
 * b &amp;&amp; true
 * </pre>
 *
 * to
 *
 * <pre>
 * b
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class InfixConditionalAndTrue extends Wring.ReplaceCurrentNode<InfixExpression> {
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }
  @Override Expression replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, true);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return Is.conditionalAnd(e) && Have.trueLiteral(extract.allOperands(e));
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE;
  }
}
