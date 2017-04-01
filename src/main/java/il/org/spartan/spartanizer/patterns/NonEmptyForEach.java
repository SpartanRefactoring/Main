package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** A non-empty for loop pattern
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class NonEmptyForEach extends ForStatementPattern {
  private static final long serialVersionUID = -4811624255870812698L;
  protected Statement lastStatement;

  public NonEmptyForEach() {
    andAlso(new Proposition.Singleton("Applicable only on non empty for loops", () -> {
      lastStatement = extract.lastStatement(current);
      return !iz.emptyStatement(lastStatement);
    }));
  }
}
