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
public final class InfixAdditionNeutralElement extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression x) {
    return "Remove 0 from  " + x;
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != PLUS ? null : replacement(extract.allOperands(x));
  }

  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal0(¢))
        $.add(¢);
    return $.size() == xs.size() ? null
        : $.isEmpty() ? duplicate.of(lisp.first(xs)) : $.size() == 1 ? duplicate.of(lisp.first($)) : subject.operands($).to(PLUS);
  }
}
