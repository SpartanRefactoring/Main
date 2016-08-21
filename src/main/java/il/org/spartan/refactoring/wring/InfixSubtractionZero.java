package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>X-0</code> by <code>X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression e) {
    return "Remove substraction of 0 in " + e;
  }

  @Override ASTNode replacement(final InfixExpression e) {
    return e.getOperator() != MINUS ? null : replacement(extract.allOperands(e));
  }

  private static ASTNode replacement(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : es)
      if (!isLiteralZero(¢))
        $.add(¢);
    if (isLiteralZero(first(es))) {
      Expression e = first($);
      if (e != null) {
        $.remove(0);
        $.add(0, subject.operand(e).to(PrefixExpression.Operator.MINUS));
      }
    }
    return $.size() == es.size() ? null // Nothing was omitted
        : $.size() == 0 ? duplicate(first(es)) // This must be a zero element
            : $.size() == 1 ? duplicate(first($)) // one element; must be in the
                                                  // right form
                : subject.operands($).to(MINUS);
  }
}
