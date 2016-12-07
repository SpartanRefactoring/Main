package il.org.spartan.spartanizer.tipping;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;

public abstract class InfixExpressionSortingRest extends InfixExpressionSorting {
  @Override public final boolean prerequisite(final InfixExpression ¢) {
    if (!suitable(¢))
      return false;
    final List<Expression> $ = extract.allOperands(¢);
    return $.size() > 2 && !Tippers.mixedLiteralKind($) && sort(chop($));
  }

  @Override public final Expression replacement(final InfixExpression $) {
    final List<Expression> operands = extract.allOperands($);
    final Expression first = operands.remove(0);
    if (!sort(operands))
      return null;
    operands.add(0, first);
    return subject.operands(operands).to($.getOperator());
  }
}