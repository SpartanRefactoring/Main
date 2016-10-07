package il.org.spartan.spartanizer.tipping;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

public abstract class InfixExpressionSortingFull extends InfixExpressionSorting {
  @Override public final boolean prerequisite(final InfixExpression x) {
    if (!suitable(x))
      return false;
    final List<Expression> es = extract.allOperands(x);
    return !Tippers.mixedLiteralKind(es) && sort(es);
  }

  @Override public Expression replacement(final InfixExpression x) {
    final List<Expression> operands = extract.allOperands(x);
    return !sort(operands) ? null : subject.operands(operands).to(x.getOperator());
  }
}