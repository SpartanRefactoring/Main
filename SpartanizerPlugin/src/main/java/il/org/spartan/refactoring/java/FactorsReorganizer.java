package il.org.spartan.refactoring.java;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.builder.*;
import il.org.spartan.refactoring.engine.*;

public class FactorsReorganizer {
  public static Expression simplify(final InfixExpression e) {
    return build(new FactorsCollector(e));
  }

  private static Expression build(final FactorsCollector c) {
    return build(c.multipliers(), c.dividers());
  }

  private static Expression build(final List<Expression> multipliers, final List<Expression> dividers) {
    return buildDividers(buildMultipliers(multipliers), dividers);
  }

  private static Expression buildDividers(final Expression first, final List<Expression> rest) {
    if (first == null)
      return buildDividers(rest);
    switch (rest.size()) {
      case 0:
        return first;
      default:
        rest.add(0, first);
        return subject.operands(rest).to(wizard.DIVIDE);
    }
  }

  private static Expression buildDividers(final List<Expression> es) {
    final Expression one = lisp.first(es).getAST().newNumberLiteral("1");
    final Expression $ = subject.pair(one, lisp.first(es)).to(wizard.DIVIDE);
    if (es.size() == 1)
      return $;
    es.remove(0);
    es.add(0, $);
    return subject.operands(es).to(wizard.DIVIDE);
  }

  private static Expression buildMultipliers(final List<Expression> es) {
    switch (es.size()) {
      case 0:
        return null;
      case 1:
        return lisp.first(es);
      case 2:
        return subject.pair(lisp.first(es), lisp.second(es)).to(wizard.TIMES);
      default:
        return subject.operands(es).to(wizard.TIMES);
    }
  }
}
