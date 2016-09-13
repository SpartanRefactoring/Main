package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public final class InfixConditionalAndTrue extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }

  @Override public Expression replacement(final InfixExpression x) {
    return Wrings.eliminateLiteral(x, true);
  }

  @Override public boolean scopeIncludes(final InfixExpression x) {
    return iz.conditionalAnd(x) && have.trueLiteral(extract.allOperands(x));
  }
}
