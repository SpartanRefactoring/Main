package org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Have;
import org.spartan.refactoring.utils.Is;

/**
 * A {@link Wring} to covert <code>b && true</code> to <code>b</code>
 *
 * @author Yossi Gil
 * @since 2015-07-20
 */
public final class InfixConditionalAndTrue extends Wring.ReplaceCurrentNode<InfixExpression> {
  @Override Expression replacement(final InfixExpression e) {
    return Wrings.eliminateLiteral(e, true);
  }
  @Override boolean scopeIncludes(final InfixExpression e) {
    return Is.conditionalAnd(e) && Have.trueLiteral(Extract.allOperands(e));
  }
  @Override String description(@SuppressWarnings("unused") final InfixExpression _) {
    return "Remove 'true' argument to '&&'";
  }
}