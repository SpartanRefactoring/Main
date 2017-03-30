package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/**
 * TODO Yuval Simon: document class 
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30
 */
public abstract class InfixExprezzion extends AbstractPattern<InfixExpression> {
  private static final long serialVersionUID = -5549618171755635448L;
  protected Expression left, right;
  protected InfixExpression.Operator operator;
  
  public InfixExprezzion() {
    andAlso(Proposition.of("Must be infix expression", () -> {
      left = step.left(current);
      right = step.right(current);
      operator = step.operator(current);
      return true;
    }));
  }
}
