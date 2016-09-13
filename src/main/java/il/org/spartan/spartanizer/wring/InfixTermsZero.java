package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Replace <code>0+X</code>, <code>X+0</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class InfixTermsZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal0(¢))
        $.add(¢);
    return $.size() == xs.size() ? null
        : $.isEmpty() ? duplicate.of(first(xs)) : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(PLUS);
  }

  @Override public String description(final InfixExpression x) {
    return "Remove all additions and substructions of 0 to and from " + x;
  }

  @Override public ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != PLUS || !stringType.isNot(x) ? null : replacement(extract.allOperands(x));
  }
}