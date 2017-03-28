package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;

/** see issue #719 for more details
 * @author koralchapnik
 * @author yaelAmitay
 * @since 16-11-04 */
public class Issue0719 {
  @Test @SuppressWarnings("static-method") public void checkFiveDefinitionsNested() {
    assert determineIf.definesManyVariables((MethodDeclaration) make
        .ast("public void methodZeroDefinition() {int x, y, z; boolean b = True; Object o = new Object(){ Double d = 0.0;}; } "), 5);
  }

  @Test @SuppressWarnings("static-method") public void checkFourDefinitionsWithAssignment() {
    assert determineIf.definesManyVariables((MethodDeclaration) make.ast("public void methodZeroDefinition() {int x, y, z; boolean b = True;} "), 4);
  }

  @Test @SuppressWarnings("static-method") public void checkOneDefinitions() {
    assert !determineIf.definesManyVariables((MethodDeclaration) make.ast("public void methodZeroDefinition() {int x;} "), 2);
  }

  @Test @SuppressWarnings("static-method") public void checkThreeDefinitionsSameLine() {
    assert determineIf.definesManyVariables((MethodDeclaration) make.ast("public void methodZeroDefinition() {int x, y, z;} "), 3);
  }

  @Test @SuppressWarnings("static-method") public void checkTwoDefinitions() {
    assert determineIf.definesManyVariables((MethodDeclaration) make.ast("public void methodZeroDefinition() {int x; int y;} "), 2);
  }

  @Test @SuppressWarnings("static-method") public void checkZeroDefinitions() {
    assert determineIf.definesManyVariables((MethodDeclaration) make.ast("public void methodZeroDefinition() {} "), 0);
  }

  @Test @SuppressWarnings("static-method") public void nullCheck() {
    assert !determineIf.definesManyVariables(null, 0);
  }

  @Test @SuppressWarnings("static-method") public void test() {
    assert true;
  }
}
