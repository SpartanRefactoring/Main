package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>1*X</code> by <code>X</code>
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal1(¢))
        $.add(¢);
    return $.size() == xs.size() ? null
        : $.isEmpty() ? duplicate.of(lisp.first(xs)) : $.size() == 1 ? duplicate.of(lisp.first($)) : subject.operands($).to(TIMES);
  }

  @Override String description(final InfixExpression x) {
    return "Remove all multiplications by 1 from " + x;
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != TIMES ? null : replacement(extract.allOperands(x));
  }
}
