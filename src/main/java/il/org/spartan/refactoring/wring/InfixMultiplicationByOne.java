package il.org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
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
      if (!iz.literal1(¢))
        $.add(¢);
    return $.size() == es.size() ? null
        : $.isEmpty() ? duplicate.of(lisp.first(es)) : $.size() == 1?duplicate.of(lisp.first($)):subject.operands($).to(TIMES);
  }
}
