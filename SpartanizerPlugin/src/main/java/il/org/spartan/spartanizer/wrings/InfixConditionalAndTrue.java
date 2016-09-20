package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
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
public final class InfixConditionalAndTrue extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }

  @Override public boolean prerequisite(final InfixExpression ¢) {
    return iz.conditionalAnd(¢) && have.trueLiteral(extract.allOperands(¢));
  }

  @Override public Expression replacement(final InfixExpression ¢) {
    return Wrings.eliminateLiteral(¢, true);
  }
}
