package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

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
public final class InfixConditionalOrFalse extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'false' argument to '||'";
  }

  @Override Expression replacement(final InfixExpression x) {
    return Wrings.eliminateLiteral(x, false);
  }

  @Override boolean scopeIncludes(final InfixExpression x) {
    return iz.conditionalOr(x) && have.falseLiteral(extract.allOperands(x));
  }
}
