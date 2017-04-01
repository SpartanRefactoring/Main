package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

 /** Abstract Pattern of {@link PostfixExpression}
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-31 */
public abstract class PostfixExprezzion extends AbstractPattern<PostfixExpression> {
  private static final long serialVersionUID = -5549618171755635448L;
  protected Expression operand;
  protected PostfixExpression.Operator operator;

  public PostfixExprezzion() {
    andAlso("Must be infix expression", () -> {
      operand = current.getOperand();
      operator = current.getOperator();
      return operand != null && operator != null;
    });
  }
}
