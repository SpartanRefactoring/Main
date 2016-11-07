package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #716 for more details
 * @author Ron Gatenio
 * @author Roy Shchory
 * @since 16-11-02 */
@SuppressWarnings("static-method") public class Issue716 {
  @Test public void nullFalse() {
    assertFalse(determineIf.hasManyStatements(null));
  }
  
  @Test public void checkNoStatements() {
    assertTrue(determineIf.hasManyStatements(
        (MethodDeclaration) wizard.ast("public void noStatements() {} ")));
  }
}
