package il.org.spartan.refactoring.java;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

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
    if (rest.isEmpty())
      return first;
    rest.add(0, first);
    return subject.operands(rest).to(wizard.MINUS2);
  }

  private static Expression buildMinus(final List<Expression> es) {
    final Expression $ = subject.operand(lisp.first(es)).to(wizard.MINUS1);
    if (es.size() == 1)
      return $;
    es.remove(0);
    es.add(0, $);
    return subject.operands(es).to(wizard.MINUS2);
  }

  private static Expression buildPlus(final List<Expression> es) {
    switch (es.size()) {
      case 0:
        return null;
      case 1:
        return lisp.first(es);
      case 2:
        return subject.pair(lisp.first(es), lisp.second(es)).to(wizard.PLUS2);
      default:
        return subject.operands(es).to(wizard.PLUS2);
    }
  }
}
