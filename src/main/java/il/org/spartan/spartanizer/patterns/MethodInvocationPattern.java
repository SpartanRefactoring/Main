package il.org.spartan.spartanizer.patterns;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tippers.*;

/** TODO Abstract Pattern for Method Invocation
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class MethodInvocationPattern extends NodePattern<MethodInvocation> {
  private static final long serialVersionUID = 0x319433183A0507AEL;
  @Property protected Expression receiver;
  @Property protected List<Expression> arguments;
  @Property protected SimpleName name;

  protected MethodInvocationPattern() {
    property("Receiver", () -> receiver = current.getExpression());
    property("Arguments", () -> arguments = arguments(current));
    property("Name", () -> name = current.getName());
  }
}
