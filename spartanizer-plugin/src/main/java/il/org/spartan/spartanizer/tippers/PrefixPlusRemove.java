package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code int i = +0} with {@code int i = 0}, {@code int i = +1} with
 * {@code int i = 1} {@code int i = +a} with {@code int i = a}, etc.
 * @author Matteo Orru'
 * @since 2016 */
public final class PrefixPlusRemove extends ReplaceCurrentNode<PrefixExpression>//
    implements Category.Theory.Arithmetics.Symbolic, Category.Transformation.Prune {
  private static final long serialVersionUID = -0x6A8336BC75EFCFE3L;

  @Override public String description(final PrefixExpression ¢) {
    return "Remove unary + in " + ¢;
  }
  @Override public ASTNode replacement(final PrefixExpression ¢) {
    return ¢.getOperator() != PLUS ? null : make.plant(copy.of(heart(¢.getOperand()))).into(¢.getParent());
  }
  private Expression heart(final Expression ¢) {
    if (iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION))
      return heart(step.expression(¢));
    final PrefixExpression $ = az.prefixExpression(¢);
    return $ == null || $.getOperator() != PLUS ? ¢ : heart($.getOperand());
  }
}
