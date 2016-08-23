package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class TermsReorganizer {
  public static Expression simplify(final InfixExpression e) {
    return build(new TermsCollector(e));
  }

  private static Expression build(final TermsCollector c) {
    return build(c.plus(), c.minus());
  }

  private static Expression build(final List<Expression> plus, final List<Expression> minus) {
    return buildMinus(buildPlus(plus), minus);
  }

  private static Expression buildMinus(final Expression first, final List<Expression> rest) {
    if (first == null)
      return buildMinus(rest);
    switch (rest.size()) {
      case 0:
        return first;
      default:
        rest.add(0, first);
        return subject.operands(rest).to(MINUS2);
    }
  }

  private static Expression buildMinus(final List<Expression> es) {
    final Expression $ = subject.operand(first(es)).to(MINUS1);
    if (es.size() == 1)
      return $;
    es.remove(0);
    es.add(0, $);
    return subject.operands(es).to(MINUS2);
  }

  private static Expression buildPlus(final List<Expression> es) {
    switch (es.size()) {
      case 0:
        return null;
      case 1:
        return first(es);
      case 2:
        return subject.pair(first(es), second(es)).to(PLUS2);
      default:
        return subject.operands(es).to(PLUS2);
    }
  }
}
