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
  @Test public void checkTenStatements() {
    assertTrue(determineIf.hasManyStatements(
        (MethodDeclaration) wizard.ast("public void tenStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i; int j;} ")));
  }
  @Test public void checkNoStatements() {
    assertFalse(determineIf.hasManyStatements((MethodDeclaration) wizard.ast("public void noStatements() { }")));
  }
  @Test public void checkFiveStatements() {
    assertFalse(determineIf.hasManyStatements((MethodDeclaration) wizard.ast("public void fiveStatements() {int a; int b; int c; int d; int e;}")));
  }
  @Test public void checkElevenStatements() {
    assertTrue(determineIf.hasManyStatements((MethodDeclaration) wizard
        .ast("public void elevenStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i; int j; int k;} ")));
  }
  @Test public void checkNineStatements() {
    assertFalse(determineIf.hasManyStatements(
        (MethodDeclaration) wizard.ast("public void nineStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i;} ")));
  }
  @Test public void checkWithIf() {
    assertFalse(determineIf.hasManyStatements(
        (MethodDeclaration) wizard.ast("public void withIf() {" + "int a = 1;" + "if (a == 2) {" + "    ++a; ++a; ++a; ++a; ++a; ++a;} }")));
    assertTrue(determineIf.hasManyStatements(
        (MethodDeclaration) wizard.ast("public void withIf() {" + "int a = 1;" + "if (a == 2) {" + "    ++a; ++a; ++a; ++a; ++a; ++a; ++a;} }")));
  }
}
