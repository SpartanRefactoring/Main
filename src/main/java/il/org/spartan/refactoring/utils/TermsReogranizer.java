package il.org.spartan.refactoring.utils;

import java.util.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

public class TermsReogranizer {
  public static Expression simplify(InfixExpression e) {
    return build(new TermsCollector(e));
  }

  private static Expression build(TermsCollector c) {
    return build(c.plus, c.minus);
  }

  private static Expression build(List<Expression> plus, List<Expression> minus) {
    return buildMinus(buildPlus(plus), minus);
  }

  private static Expression buildMinus(Expression first, List<Expression> rest) {
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

  private static Expression buildMinus(List<Expression> rest) {
    return subject.operands(rest).to(MINUS2);
  }

  private static Expression buildPlus(List<Expression> es) {
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
