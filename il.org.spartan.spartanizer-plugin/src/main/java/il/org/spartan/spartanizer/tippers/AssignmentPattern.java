package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-31 */
public abstract class AssignmentPattern extends NodePattern<Assignment> {
  private static final long serialVersionUID = 0x6BAA0D9033D78EDEL;
  protected Expression to, from;
  protected Operator operator;

  public AssignmentPattern() {
    andAlso("Must be legal assignment", () -> {
      to = current.getLeftHandSide();
      from = current.getLeftHandSide();
      operator = current.getOperator();
      return to != null && from != null && operator != null;
    });
  }
}
