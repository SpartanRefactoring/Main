package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>a+0</code> by <code>a</code>
 * @author Yossi Gil
 * @since 2015-09-05 */
// This one is working at least for tests I just wrote, and not breaking any others.
// Didn't want to remove Mateu's code still checked it and it's not working in some tests 
// I think it should. This one works (I changed some tests to the way I think they should be look at the commit)
// So I left the decision to you.
public final class InfixAdditionNeutralElement extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal0(¢))
        $.add(¢);
    return $.size() == xs.size() ? null
        : $.isEmpty() ? duplicate.of(first(xs)) : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(PLUS);
  }

  @Override String description(final InfixExpression e) {
    return "Remove 0 from  " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != PLUS ? null : replacement(extract.allOperands(e));
  }
}
