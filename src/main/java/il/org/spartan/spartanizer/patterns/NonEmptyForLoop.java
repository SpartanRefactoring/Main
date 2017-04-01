package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** A non-empty for loop pattern
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class NonEmptyForLoop extends AbstractPattern<ForStatement> {
  private static final long serialVersionUID = -6486213170578817738L;
  protected Statement forBody;
  protected Expression forCondition;
  protected Statement lastStatement;

  public NonEmptyForLoop() {
    andAlso(new Proposition.Singleton("Applicable only on non empty for loops", () -> {
      lastStatement = extract.lastStatement(current);
      if (iz.emptyStatement(lastStatement))
        return false;
      forBody = step.body(current);
      forCondition = current.getExpression();
      return true;
    }));
  }

  protected Statement forBody() {
    return forBody;
  }

  protected Expression forCondition() {
    return forCondition;
  }
}
