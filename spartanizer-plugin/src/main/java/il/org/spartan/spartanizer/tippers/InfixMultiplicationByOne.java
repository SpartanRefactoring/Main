package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.toList;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code 1*X} by {@code X}
 * @author Yossi Gil
 * @since 2015-09-05 */
public final class InfixMultiplicationByOne extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Transformation.Prune, Category.Theory.Arithmetics {
  private static final long serialVersionUID = 0x7AAE85AAEB6D72C8L;

  private static ASTNode replacement(final List<Expression> ¢) {
    final List<Expression> $ = ¢.stream().filter(λ -> !iz.literal1(λ)).collect(toList());
    return $.size() == ¢.size() ? null
        : $.isEmpty() ? copy.of(the.firstOf(¢)) : $.size() == 1 ? copy.of(the.firstOf($)) : subject.operands($).to(TIMES);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Remove all multiplications by 1 from " + ¢;
  }
  @Override public ASTNode replacement(final InfixExpression ¢) {
    return ¢.getOperator() != TIMES ? null : replacement(extract.allOperands(¢));
  }
}
