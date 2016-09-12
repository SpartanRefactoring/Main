package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** Convert <code>""+x</code> to <code>x+""</code>
 * @author Dan Greenstein
 * @since 2016 */
public class InfixConcatenationEmptyStringLeft extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.Canonicalization {

  private static InfixExpression replace(final InfixExpression x) {
    return subject.pair(duplicate.of(right(x)), duplicate.of(left(x))).to(wizard.PLUS2);
  }

  @SuppressWarnings("unused") @Override String description(final InfixExpression x) {
    return "Switches the empty string in empty string addition from the left operand to the right";
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return !iz.emptyStringLiteral(left(x)) || !iz.infixPlus(x) ? null : replace(x);
  }
}
