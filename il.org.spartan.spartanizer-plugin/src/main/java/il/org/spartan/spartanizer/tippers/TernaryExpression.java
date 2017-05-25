package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** A pattern for ConditionalExpression (i.e ternary)
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public abstract class TernaryExpression extends NodePattern<ConditionalExpression> {
  private static final long serialVersionUID = 0xBD3AADEC1A65EFFL;
  protected Expression condition, then, elze;

  public TernaryExpression() {
    andAlso(Proposition.that("Must be ternary expression", () -> {
      condition = step.expression(current);
      then = step.then(current);
      elze = step.elze(current);
      return true;
    }));
  }
}
