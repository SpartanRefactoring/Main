package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.hop.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code X-0} by {@code X} and {@code 0-X} by {@code -X}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static final long serialVersionUID = -2150446855942062458L;

  private static List<Expression> minusFirst(final List<Expression> prune) {
    return cons(make.minus(first(prune)), chop(prune));
  }

  private static List<Expression> prune(final Collection<Expression> ¢) {
    final List<Expression> $ = ¢.stream().filter(λ -> !iz.literal0(λ)).collect(toList());
    return $.size() != ¢.size() ? $ : null;
  }

  private static ASTNode replacement(final List<Expression> xs) {
    final List<Expression> $ = prune(xs);
    if ($ == null)
      return null;
    final Expression first = first(xs);
    if ($.isEmpty())
      return make.from(first).literal(0);
    assert !$.isEmpty();
    if ($.size() == 1)
      return !iz.literal0(first) ? first : make.minus(first($));
    assert $.size() >= 2;
    return subject.operands(!iz.literal0(first) ? $ : minusFirst($)).to(MINUS2);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove subtraction of 0 in " + ¢;
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != MINUS ? null : replacement(operands(¢));
  }
}
