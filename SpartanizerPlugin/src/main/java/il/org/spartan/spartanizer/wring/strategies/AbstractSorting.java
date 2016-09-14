package il.org.spartan.spartanizer.wring.strategies;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

abstract class AbstractSorting extends ReplaceCurrentNode<InfixExpression> {
  @Override protected final String description(final InfixExpression ¢) {
    return "Reorder operands of " + ¢.getOperator();
  }

  protected abstract boolean sort(List<Expression> operands);
}