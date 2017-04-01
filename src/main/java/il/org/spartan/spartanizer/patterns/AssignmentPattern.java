package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-31 */
public abstract class AssignmentPattern extends AbstractPattern<Assignment> {
  private static final long serialVersionUID = 7758028221095317214L;
  protected Expression left, right;
  protected Operator operator;

  public AssignmentPattern() {
    andAlso("Must bre legal assignment", () -> {
      left = current.getLeftHandSide();
      right = current.getLeftHandSide();
      operator = current.getOperator();
      return left != null && right != null && operator != null;
    });
  }
}
