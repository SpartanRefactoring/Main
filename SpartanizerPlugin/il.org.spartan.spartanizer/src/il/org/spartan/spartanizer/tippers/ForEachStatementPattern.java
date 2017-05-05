package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

/** A Pattern for ForEach Statement
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class ForEachStatementPattern extends NodePattern<EnhancedForStatement> {
  private static final long serialVersionUID = 0x66150EA5A0263DA9L;
  protected Expression forEachExpression;
  protected Statement body;

  public ForEachStatementPattern() {
    andAlso("Must be a legal ForEach statement", () -> {
      forEachExpression = current.getExpression();
      body = current.getBody();
      return body != null && forEachExpression != null;
    });
  }
}
