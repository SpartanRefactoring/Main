package il.org.spartan.spartanizer.wring;
import static il.org.spartan.spartanizer.ast.step.*;

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
  private static boolean isEmptyStringLiteral(final Expression x) {
    return wizard.same(x, x.getAST().newStringLiteral());
  }

  private static InfixExpression replace(final InfixExpression x) {
    return subject.pair(duplicate.of(right(x)), duplicate.of(left(x))).to(wizard.PLUS2);
  }

  @SuppressWarnings("unused") @Override String description(final InfixExpression x) {
    return "Switches the empty string in empty string addition from the left operand to the right";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return !isEmptyStringLiteral(left(x)) || !iz.infixPlus(x) ? null : replace(x);
  }
}
