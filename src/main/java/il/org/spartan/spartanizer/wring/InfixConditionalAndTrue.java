package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

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
public final class InfixConditionalAndTrue extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Remove 'true' argument to '&&'";
  }

  @Override Expression replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, true);
  }

  @Override boolean scopeIncludes(final InfixExpression e) {
    return iz.conditionalAnd(e) && have.trueLiteral(extract.allOperands(e));
  }
}
