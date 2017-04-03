package il.org.spartan.spartanizer.patterns;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Pattern for For Statement
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @author Raviv Rachmiel
 * @since 2017-03-31 */
public abstract class ForStatementPattern extends AbstractPattern<ForStatement> {
  private static final long serialVersionUID = 0x6BAA0D9033D78EDEL;
  protected Expression forExpression;
  protected Statement body;

  public ForStatementPattern() {
    andAlso("Must bre legal ForStatement", () -> {
      forExpression = current.getExpression();
      body = step.body(current);
      return body != null && forExpression != null;
    });
  }

  protected Statement forBody() {
    return body;
  }

  protected Expression forCondition() {
    return forExpression;
  }
}
