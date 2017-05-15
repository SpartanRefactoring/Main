package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Either constructor or {@link MethodDeclaration}
 * @author Yossi Gil
 * @since 2017-04-22 */
public abstract class CallablePattern extends NodePattern<MethodDeclaration> {
  private static final long serialVersionUID = 1;
  protected List<SingleVariableDeclaration> parameters;
  protected SimpleName name;
  protected Block body;

  public CallablePattern() {
    property("parameters", () -> parameters = parameters(current));
    property("name", () -> name = current.getName());
    property("body", () -> body = current.getBody());
  }
}
