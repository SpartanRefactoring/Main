package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert
 *
 * <pre>
 * b || false
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
public final class InfixConditionalOrFalse extends ReplaceCurrentNode<InfixExpression> implements Kind.InVain {
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'false' argument to '||'";
  }

  @Override public boolean prerequisite(final InfixExpression ¢) {
    return iz.conditionalOr(¢) && have.falseLiteral(extract.allOperands(¢));
  }

  @Override public Expression replacement(final InfixExpression ¢) {
    return Wrings.eliminateLiteral(¢, false);
  }
}
