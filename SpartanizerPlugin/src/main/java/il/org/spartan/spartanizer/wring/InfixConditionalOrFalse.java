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
public final class InfixConditionalOrFalse extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'false' argument to '||'";
  }

  @Override Expression replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, false);
  }

  @Override boolean scopeIncludes(final InfixExpression e) {
    return iz.conditionalOr(e) && have.falseLiteral(extract.allOperands(e));
  }
}
