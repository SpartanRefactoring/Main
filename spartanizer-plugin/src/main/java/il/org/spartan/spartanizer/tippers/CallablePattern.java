package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

/** Either constructor or {@link MethodDeclaration}
 * @author Yossi Gil
 * @since 2017-04-22 */
public abstract class CallablePattern extends NodeMatcher<MethodDeclaration> {
  private static final long serialVersionUID = 1;
  protected List<SingleVariableDeclaration> parameters;
  protected SimpleName name;
  protected Block body;
  protected Javadoc jdoc;

  public CallablePattern() {
    property("parameters", () -> parameters = parameters(current));
    property("name", () -> name = current.getName());
    property("body", () -> body = current.getBody());
    property("JavaDOC", () -> jdoc = current.getJavadoc());
  }
}
