package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #718 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-03 */
public class Issue718 {
  MethodDeclaration loaded = (MethodDeclaration) methodDeclerationFromString("public void f(int x, int y, int z){ String a, b, c, d, e, f;}");

  @SuppressWarnings("static-method") @Test public void checkIfCompiles() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void checkIfReturnTypeIsBoolean() {
    @SuppressWarnings("unused") final boolean b = determineIf.loaded(null);
  }

  @Test public void checkIfLoadedMethodReturnsTrue() {
    @SuppressWarnings("unused") boolean b = determineIf.loaded(loaded);
  }

  private static ASTNode methodDeclerationFromString(String ¢) {
    return wizard.ast(¢);
  }
}
