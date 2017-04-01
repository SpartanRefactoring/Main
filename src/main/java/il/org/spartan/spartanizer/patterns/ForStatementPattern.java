package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

/** TODO dormaayn: Pattern for For Statement
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-31 */
public abstract class ForStatementPattern extends AbstractPattern<ForStatement> {
  private static final long serialVersionUID = 7758028221095317214L;
  protected Expression forExpression;
  protected Statement body;

  public ForStatementPattern() {
    andAlso("Must bre legal ForStatement", () -> {
      forExpression = current.getExpression();
      body = current.getBody();
      return body != null && forExpression != null;
    });
  }
}
