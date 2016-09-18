package il.org.spartan.spartanizer.wringing;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;

public abstract class InfixSortingOfCDR extends AbstractSorting {
  @Override public boolean canSuggest(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    es.remove(0);
    return !Wrings.mixedLiteralKind(es) && sort(es);
  }

  @Override public Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    final Expression first = operands.remove(0);
    if (!sort(operands))
      return null;
    operands.add(0, first);
    return subject.operands(operands).to(x.getOperator());
  }
}