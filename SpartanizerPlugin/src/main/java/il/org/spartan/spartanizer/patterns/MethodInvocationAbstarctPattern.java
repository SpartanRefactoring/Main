package il.org.spartan.spartanizer.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO Abstract Pattern for Method Invocation
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class MethodInvocationAbstarctPattern extends AbstractPattern<MethodInvocation> {
  private static final long serialVersionUID = 0x319433183A0507AEL;
  @Property protected Expression expression;
  @Property protected List<Expression> arguments;
  @Property protected SimpleName name;

  public MethodInvocationAbstarctPattern() {
    andAlso("Must be a Method Invocation", () -> {
      expression = current.getExpression();
      arguments = step.arguments(current);
      name = current.getName();
      return name != null;
    });
  }

}
