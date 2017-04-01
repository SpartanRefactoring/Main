package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** TODO Yuval Simon: document class
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @since 2017-03-30 */
public abstract class TernaryExpression extends AbstractPattern<ConditionalExpression> {
  private static final long serialVersionUID = 852212628211523327L;
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
