package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #716 for more details
 * @author Ron Gatenio
 * @author Roy Shchory
 * @since 16-11-02 */
@SuppressWarnings("static-method")
public class Issue0716 {
  @Test public void checkElevenStatements() {
    assert determineIf.hasManyStatements((MethodDeclaration) make
        .ast("public void elevenStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i; int j; int k;} "));
  }
  @Test public void checkFiveStatements() {
    assert !determineIf.hasManyStatements((MethodDeclaration) make.ast("public void fiveStatements() {int a; int b; int c; int d; int e;}"));
  }
  @Test public void checkNineStatements() {
    assert !determineIf.hasManyStatements(
        (MethodDeclaration) make.ast("public void nineStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i;} "));
  }
  @Test public void checkNoStatements() {
    assert !determineIf.hasManyStatements((MethodDeclaration) make.ast("public void noStatements() { }"));
  }
  @Test public void checkTenStatements() {
    assert determineIf.hasManyStatements(
        (MethodDeclaration) make.ast("public void tenStatements() {int a; int b; int c; int d; int e; int f; int g; int h; int i; int j;} "));
  }
  @Test public void checkWithIf() {
    assert !determineIf
        .hasManyStatements((MethodDeclaration) make.ast("public void withIf() {int a = 1;if (a == 2) {    ++a; ++a; ++a; ++a; ++a; ++a;} }"));
    assert determineIf
        .hasManyStatements((MethodDeclaration) make.ast("public void withIf() {int a = 1;if (a == 2) {    ++a; ++a; ++a; ++a; ++a; ++a; ++a;} }"));
  }
  @Test public void nullFalse() {
    assert !determineIf.hasManyStatements(null);
  }
}
