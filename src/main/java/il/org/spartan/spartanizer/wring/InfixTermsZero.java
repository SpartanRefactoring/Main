package il.org.spartan.spartanizer.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>0+X</code>, <code>X+0</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixTermsZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static ASTNode replacement(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (!iz.literal0(¢))
        $.add(¢);
    return $.size() == es.size() ? null
        : $.isEmpty() ? duplicate.of(lisp.first(es)) : $.size() == 1 ? duplicate.of(lisp.first($)) : subject.operands($).to(PLUS);
  }

  @Override String description(final InfixExpression e) {
    return "Remove all additions and substructions of 0 to and from " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != PLUS ? null : replacement(extract.allOperands(e));
  }
}