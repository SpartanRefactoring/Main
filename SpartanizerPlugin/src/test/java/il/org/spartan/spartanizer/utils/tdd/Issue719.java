package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #719 for more details
 * @author koralchapnik
 * @author yaelAmitay
 * @since 16-11-04 */
public class Issue719 {
  @SuppressWarnings("static-method") @Test public void test() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void nullCheck() {
    assertFalse(determineIf.definesManyVariables(null, 0));
  }

  @SuppressWarnings("static-method") @Test public void checkZeroDefinition() {
    assertTrue(determineIf.definesManyVariables(((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {} ")), 0));
  }
}
