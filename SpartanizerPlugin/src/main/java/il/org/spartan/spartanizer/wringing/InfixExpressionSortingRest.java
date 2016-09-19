package il.org.spartan.spartanizer.wringing;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;

public abstract class InfixExpressionSortingRest extends InfixExpressionSorting {
  @Override public final boolean canSuggest(final InfixExpression x) {
    if (!suitable(x))
      return false;
    final List<Expression> es = extract.allOperands(x);
    if (es.size() <= 2)
      return false;
    return !Wrings.mixedLiteralKind(es) && sort(chop(es));
  }

  @Override public final Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    final Expression first = operands.remove(0);
    if (!sort(operands))
      return null;
    operands.add(0, first);
    return subject.operands(operands).to(x.getOperator());
  }
}