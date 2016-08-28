package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>1*X</code> by <code>X</code>
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove all multiplications by 1 from " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != TIMES ? null : replacement(extract.allOperands(e));
  }

  private static ASTNode replacement(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (!isLiteralOne(¢))
        $.add(¢);
    return $.size() == es.size() ? null : $.isEmpty() ? duplicate(first(es)) : $.size() == 1 ? duplicate(first($)) : subject.operands($).to(TIMES);
  }

  private static boolean isLiteralOne(final Expression ¢) {
    return isLiteralOne(asNumberLiteral(¢));
  }

  private static boolean isLiteralOne(final NumberLiteral ¢) {
    return ¢ != null && isLiteralOne(¢.getToken());
  }

  private static boolean isLiteralOne(final String ¢) {
    try {
      return Integer.parseInt(¢) == 1;
    } catch (@SuppressWarnings("unused") final NumberFormatException __) {
      return false;
    }
  }
}
