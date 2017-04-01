package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

/**
 * A Pattern for ForEach Statement
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01
 */
public abstract class ForEachStatementPattern extends AbstractPattern<EnhancedForStatement>{

  private static final long serialVersionUID = 7355801670898826665L;
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
