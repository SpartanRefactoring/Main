package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Pattern for For Statement
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @author Raviv Rachmiel
 * @since 2017-03-31 */
public abstract class ForStatementPattern extends NodeMatcher<ForStatement> {
  private static final long serialVersionUID = 0x6BAA0D9033D78EDEL;
  protected Expression expression;
  protected Statement body;
  protected List<Expression> initializers;
  protected List<Expression> updaters;

  public ForStatementPattern() {
    andAlso("Must be a valid for statement", () -> {
      expression = current.getExpression();
      body = step.body(current);
      initializers = step.initializers(current);
      updaters = step.updaters(current);
      return body != null;
    });
  }
}
