/** TODO:  Yossi Gil <yossi.gil@gmail.com> please add a description 
 * @author  Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 7, 2016
 */
package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;

// TOOD Niv: Who wrote this class?
final class FactorsReorganizer {
  public static Expression simplify(final InfixExpression ¢) {
    return build(new FactorsCollector(¢));
  }

  private static Expression build(final FactorsCollector ¢) {
    return build(¢.multipliers(), ¢.dividers());
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

  private static Expression buildDividers(final List<Expression> ¢) {
    final Expression $ = subject.pair(first(¢).getAST().newNumberLiteral("1"), first(¢)).to(DIVIDE);
    if (¢.size() == 1)
      return $;
    ¢.remove(0);
    ¢.add(0, $);
    return subject.operands(¢).to(DIVIDE);
  }

  private static Expression buildMultipliers(final List<Expression> ¢) {
    switch (¢.size()) {
      case 0:
        return null;
      case 1:
        return first(¢);
      case 2:
        return subject.pair(first(¢), second(¢)).to(TIMES);
      default:
        return subject.operands(¢).to(TIMES);
    }
  }
}

