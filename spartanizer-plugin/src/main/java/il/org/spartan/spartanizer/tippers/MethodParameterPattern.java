package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.safety.az;

/** Method parameter
 * @author Yossi Gil
 * @since 2017-04-22 */
public abstract class MethodParameterPattern extends FragmentMatcher {
  private static final long serialVersionUID = 1;
  protected MethodDeclaration methodDeclaration;

  public MethodParameterPattern() {
    notNil("Parent is a method declaration", () -> methodDeclaration = az.methodDeclaration(parent));
  }
}
