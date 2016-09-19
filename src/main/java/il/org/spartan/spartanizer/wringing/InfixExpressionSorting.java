package il.org.spartan.spartanizer.wringing;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

abstract class InfixExpressionSorting extends ReplaceCurrentNode<InfixExpression> {

  @Override public final String description(final InfixExpression ¢) {
    return "Reorder operands of " + ¢.getOperator();
  }

  protected abstract boolean sort(List<Expression> operands);

  protected abstract boolean suitable(InfixExpression x);
}