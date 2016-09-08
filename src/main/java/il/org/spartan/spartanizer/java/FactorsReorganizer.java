package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.utils.*;
//TOOD Niv: Who wrote this class?
public class FactorsReorganizer {
  public static Expression simplify(final InfixExpression x) {
    return build(new FactorsCollector(x));
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
    if (rest.isEmpty())
      return first;
    rest.add(0, first);
    return subject.operands(rest).to(DIVIDE);
  }

  private static Expression buildDividers(final List<Expression> xs) {
    final Expression one = lisp.first(xs).getAST().newNumberLiteral("1");
    final Expression $ = subject.pair(one, lisp.first(xs)).to(DIVIDE);
    if (xs.size() == 1)
      return $;
    xs.remove(0);
    xs.add(0, $);
    return subject.operands(xs).to(DIVIDE);
  }

  private static Expression buildMultipliers(final List<Expression> xs) {
    switch (xs.size()) {
      case 0:
        return null;
      case 1:
        return lisp.first(xs);
      case 2:
        return subject.pair(lisp.first(xs), lisp.second(xs)).to(TIMES);
      default:
        return subject.operands(xs).to(TIMES);
    }
  }
}
