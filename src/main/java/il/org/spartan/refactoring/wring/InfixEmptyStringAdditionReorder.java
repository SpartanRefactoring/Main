package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

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
  private static boolean isEmptyStringLiteral(final Expression x) {
    return wizard.same(x, x.getAST().newStringLiteral());
  }

  private static InfixExpression replace(final InfixExpression x) {
    return subject.pair(duplicate.of(step.right(x)), duplicate.of(step.left(x))).to(wizard.PLUS2);
  }

  @SuppressWarnings("unused") @Override String description(final InfixExpression x) {
    return "Switches the empty string in empty string addition from the left operand to the right";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return !isEmptyStringLiteral(step.left(x)) || !iz.infixPlus(x) ? null : replace(x);
  }
}
