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
  @SuppressWarnings("static-method") @Test public void checkZeroDefinitions() {
    assertTrue(determineIf.definesManyVariables((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {} "), 0));
  }
  @SuppressWarnings("static-method") @Test public void checkOneDefinitions() {
    assertFalse(determineIf.definesManyVariables((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {int x;} "), 2));
  }
  @SuppressWarnings("static-method") @Test public void checkTwoDefinitions() {
    assertTrue(determineIf.definesManyVariables((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {int x; int y;} "), 2));
  }
  @SuppressWarnings("static-method") @Test public void checkThreeDefinitionsSameLine() {
    assertTrue(determineIf.definesManyVariables((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {int x, y, z;} "), 3));
  }
  @SuppressWarnings("static-method") @Test public void checkFourDefinitionsWithAssignment() {
    assertTrue(
        determineIf.definesManyVariables((MethodDeclaration) wizard.ast("public void methodZeroDefinition() {int x, y, z; boolean b = True;} "), 4));
  }
  @SuppressWarnings("static-method") @Test public void checkFiveDefinitionsNested() {
    assertTrue(determineIf.definesManyVariables((MethodDeclaration) wizard
        .ast("public void methodZeroDefinition() {int x, y, z; boolean b = True; Object o = new Object(){ Double d = 0.0;}; } "), 5));
  }
}
