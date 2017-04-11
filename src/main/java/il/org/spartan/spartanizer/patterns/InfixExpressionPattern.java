package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** A pattern for InfixExpression
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public abstract class InfixExpressionPattern extends AbstractPattern<InfixExpression> {
  private static final long serialVersionUID = -0x4D043452F68C22F8L;
  protected Expression left, right;
  protected InfixExpression.Operator operator;

  public InfixExpressionPattern() {
    andAlso("Must be infix expression", () -> {
      left = step.left(current);
      right = step.right(current);
      operator = step.operator(current);
      return true;
    });
  }
}
