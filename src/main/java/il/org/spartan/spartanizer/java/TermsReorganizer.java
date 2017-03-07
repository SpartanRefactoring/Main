package il.org.spartan.spartanizer.java;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Reorganizer terms in a canonical way
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
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
    return subject.operands(rest).to(wizard.MINUS2);
  }

  private static Expression buildMinus(final List<Expression> ¢) {
    final Expression $ = subject.operand(first(¢)).to(wizard.MINUS1);
    if (¢.size() == 1)
      return $;
    ¢.remove(0);
    ¢.add(0, $);
    return subject.operands(¢).to(wizard.MINUS2);
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
