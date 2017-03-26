package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 7, 2016 */
interface FactorsReorganizer {
   static Expression simplify(final InfixExpression ¢) {
    return build(new FactorsCollector(¢));
  }

   static Expression build( final FactorsCollector ¢) {
    return build(¢.multipliers(), ¢.dividers());
  }

   static Expression build( final List<Expression> multipliers,  final List<Expression> dividers) {
    return buildDividers(buildMultipliers(multipliers), dividers);
  }

   static Expression buildDividers( final Expression first,  final List<Expression> rest) {
    if (first == null)
      return buildDividers(rest);
    if (rest.isEmpty())
      return first;
    rest.add(0, first);
    return subject.operands(rest).to(DIVIDE);
  }

  static Expression buildDividers( final List<Expression> ¢) {
    final Expression $ = subject.pair(first(¢).getAST().newNumberLiteral("1"), first(¢)).to(DIVIDE);
    if (¢.size() == 1)
      return $;
    ¢.remove(0);
    ¢.add(0, $);
    return subject.operands(¢).to(DIVIDE);
  }

  static Expression buildMultipliers( final List<Expression> ¢) {
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
