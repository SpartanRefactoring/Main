package il.org.spartan.spartanizer.wring.strategies;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;

public abstract class InfixSorting extends AbstractSorting {
  @Override public boolean eligible(final InfixExpression x) {
    final List<Expression> es = extract.allOperands(x);
    return !Wrings.mixedLiteralKind(es) && sort(es);
  }

  @Override public Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    return !sort(operands) ? null : subject.operands(operands).to(x.getOperator());
  }
}