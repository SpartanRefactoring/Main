package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.factory.make.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.hop.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code>X-0</code> by <code>X</code> and <code>0-X</code> by
 * <code>-X<code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements TipperCategory.InVain {
  private static List<Expression> minusFirst(final List<Expression> prune) {
    return cons(minus(first(prune)), chop(prune));
  }

  private static List<Expression> prune(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    for (final Expression ¢ : xs)
      if (!iz.literal0(¢))
        $.add(¢);
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
      return !iz.literal0(first) ? first : minus(first(prune));
    assert prune.size() >= 2;
    return subject.operands(!iz.literal0(first) ? prune : minusFirst(prune)).to(MINUS2);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove subtraction of 0 in " + ¢;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != MINUS ? null : replacement(operands(¢));
  }
}
