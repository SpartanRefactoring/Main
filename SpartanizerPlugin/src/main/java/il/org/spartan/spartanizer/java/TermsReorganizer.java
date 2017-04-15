package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.wizard.*;

/** Reorganizer terms in a canonical way
 * @author Yossi Gil
 * @since 2016 */
public enum TermsReorganizer {
  ;
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
    return subject.operands(rest).to(op.MINUS2);
  }

  private static Expression buildMinus(final List<Expression> ¢) {
    final Expression $ = subject.operand(first(¢)).to(op.MINUS1);
    if (¢.size() == 1)
      return $;
    ¢.remove(0);
    ¢.add(0, $);
    return subject.operands(¢).to(op.MINUS2);
  }

  private static Expression buildPlus(final List<Expression> ¢) {
    switch (¢.size()) {
      case 0:
        return null;
      case 1:
        return first(¢);
      case 2:
        return subject.pair(first(¢), second(¢)).to(op.PLUS2);
      default:
        return subject.operands(¢).to(op.PLUS2);
    }
  }
}
