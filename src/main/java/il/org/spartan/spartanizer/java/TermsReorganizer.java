package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** Reorganizer terms in a canonical way
 * @author Yossi Gil
 * @since 2016 */
public final class TermsReorganizer {
  public static Expression simplify(final InfixExpression ¢) {
    return build(new TermsCollector(¢));
  }

  private static Expression build(final List<Expression> plus, final List<Expression> minus) {
    return buildMinus(buildPlus(plus), minus);
  }

  private static Expression build(final TermsCollector ¢) {
    return build(¢.plus(), ¢.minus());
  }

  private static Expression buildMinus(final Expression first, final List<Expression> rest) {
    if (first == null)
      return buildMinus(rest);
    if (rest.isEmpty())
      return first;
    rest.add(0, first);
    return subject.operands(rest).to(wizard.MINUS2);
  }

  private static Expression buildMinus(final List<Expression> xs) {
    final Expression $ = subject.operand(first(xs)).to(wizard.MINUS1);
    if (xs.size() == 1)
      return $;
    xs.remove(0);
    xs.add(0, $);
    return subject.operands(xs).to(wizard.MINUS2);
  }

  private static Expression buildPlus(final List<Expression> ¢) {
    switch (¢.size()) {
      case 0:
        return null;
      case 1:
        return first(¢);
      case 2:
        return subject.pair(first(¢), second(¢)).to(wizard.PLUS2);
      default:
        return subject.operands(¢).to(wizard.PLUS2);
    }
  }
}
