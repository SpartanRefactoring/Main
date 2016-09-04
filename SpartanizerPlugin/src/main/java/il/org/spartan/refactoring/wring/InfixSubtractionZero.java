package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.assemble.make.*;
import static il.org.spartan.refactoring.ast.hop.*;
import static il.org.spartan.refactoring.ast.iz.*;
import static il.org.spartan.refactoring.ast.wizard.*;
import static il.org.spartan.refactoring.utils.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>X-0</code> by <code>X</code> and <code>0-X</code> by
 * <code>-X<code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static List<Expression> minusFirst(final List<Expression> prune) {
    return cons(minus(first(prune)), chop(prune));
  }

  private static List<Expression> prune(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression x : xs)
      if (!literal0(x))
        $.add(x);
    return $.size() != xs.size() ? $ : null;
  }

  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> prune = prune(xs);
    if (prune == null)
      return null;
    final Expression first = first(xs);
    if (prune.isEmpty())
      return make.from(first).literal(0);
    assert !prune.isEmpty();
    if (prune.size() == 1)
      return !literal0(first) ? first : minus(first(prune));
    assert prune.size() >= 2;
    return subject.operands(!literal0(first) ? prune : minusFirst(prune)).to(MINUS2);
  }

  @Override String description(final InfixExpression x) {
    return "Remove subtraction of 0 in " + x;
  }

  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != MINUS ? null : replacement(operands(x));
  }
}
