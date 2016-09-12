package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>1*X</code> by <code>X</code>
 * @author Yossi Gil
 * @since 2015-09-05 */
// TODO: Alex, this seems to be a duplicate of {@link InfixAdditionZero}, check
// which one is active, and if there is anything useful in the other, steal it.
public final class InfixAdditionNeutralElement extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal0(¢))
        $.add(¢);
    return $.size() == xs.size() ? null
        : $.isEmpty() ? duplicate.of(first(xs)) : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(PLUS);
  }

  @Override String description(final InfixExpression x) {
    return "Remove 0 from  " + x;
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != PLUS ? null : replacement(extract.allOperands(x));
  }
}
