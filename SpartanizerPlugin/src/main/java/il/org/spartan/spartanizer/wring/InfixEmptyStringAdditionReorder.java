package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** transforms
 *
 * <pre>
 * "" + x
 * </pre>
 *
 * to
 *
 * <pre>
 * x + ""
 * </pre>
 *
 * @author Dan Greenstein
 * @since 2016 */
public class InfixEmptyStringAdditionReorder extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {
  private static boolean isEmptyStringLiteral(final Expression e) {
    return wizard.same(e, e.getAST().newStringLiteral());
  }

  private static InfixExpression replace(final InfixExpression e) {
    return subject.pair(duplicate.of(step.right(e)), duplicate.of(step.left(e))).to(wizard.PLUS2);
  }

  @SuppressWarnings("unused") @Override String description(final InfixExpression e) {
    return "Switches the empty string in empty string addition from the left operand to the right";
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return !isEmptyStringLiteral(step.left(e)) || !iz.infixPlus(e) ? null : replace(e);
  }
}
