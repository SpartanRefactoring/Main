package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>0+X</code>, <code>X+0</code> and <code>X-0</code> by
 * <code>X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove addition/ substraction of 0 in " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != MINUS ? null : replacementMinus(e);
  }

  private static ASTNode replacementMinus(final InfixExpression e) {
    return isLiteralZero(left(e)) ? subject.operand(right(e)).to(PrefixExpression.Operator.MINUS)
        : isLiteralZero(right(e)) ? duplicate(left(e)) : null;
  }
}
