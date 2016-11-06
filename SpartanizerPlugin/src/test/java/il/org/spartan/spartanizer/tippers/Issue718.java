package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #718 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-03 */
public class Issue718 {
  MethodDeclaration loaded = (MethodDeclaration) methodDeclarationFromString("public void f(int x, int y, int z){ String a, b, c, d, e, f;}");
  MethodDeclaration notLoaded = (MethodDeclaration) methodDeclarationFromString("public void g(int x, int y, int z){ String a, b, c, d;}");
  MethodDeclaration biMethod = (MethodDeclaration) methodDeclarationFromString("public void h(int x, int y){}");
  MethodDeclaration TwoParamsFiveDefsMethod = (MethodDeclaration) methodDeclarationFromString("public void h(int x, int y){int a, b, c, d, e;}");

  @SuppressWarnings("static-method") @Test public void checkIfCompiles() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void checkIfReturnTypeIsBoolean() {
    @SuppressWarnings("unused") final boolean b = determineIf.loaded(null);
  }

  @Test public void checkIfLoadedMethodPasses() {
    assertTrue(determineIf.loaded(loaded));
  }

  @Test public void checkIfNotLoadedMethodFailes() {
    assertFalse(determineIf.loaded(notLoaded));
  }

  @Test public void checkIfBiMethodFailes() {
    assertFalse(determineIf.loaded(biMethod));
  }
  
  @Test public void checkIfThreeParamsPass() {
    assertTrue(determineIf.loaded(loaded));
  }
  
  @Test public void checkIfTripledParamsAndQuintupledVarDefsPass(){
    assertTrue(determineIf.loaded(loaded));
  }
  
  @Test public void checkIfTripledParamsAndQuadrupleVarDefsFail(){
    assertFalse(determineIf.loaded(notLoaded));
  }
  @Test public void checkIfDoubledParamsAndQuintupledVarDefsFail(){
    assertFalse(determineIf.loaded(TwoParamsFiveDefsMethod));
  }
  
  

  private static ASTNode methodDeclarationFromString(final String ¢) {
    return wizard.ast(¢);
  }
}
