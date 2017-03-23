package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Doron Mehsulam: document class
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-03-22 */
public class ConcatenateStringLiterals extends Tipper<InfixExpression> {
  private static final long serialVersionUID = -8989985850043975664L;

  @Override public Tip tip(final InfixExpression ¢) {
    return super.tip(¢);
  }

  @Override public boolean canTip(@NotNull final InfixExpression ¢) {
    return ¢.getOperator() == InfixExpression.Operator.PLUS && findTwoConsecutive();
  }

  StringLiteral first;
  StringLiteral second;

  @SuppressWarnings("static-method") private boolean findTwoConsecutive() {
    // Make sure that there are two consecutive string literals in the
    // list of extended operators. Set these two into fields.
    return false;
  }

  @Override public String description(final InfixExpression ¢) {
    return ¢ + "";
  }
}
