package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #718 for more details
 * @author Oren Afek
 * @author Amir Sagiv
 * @since 16-11-03 */
public class Issue0718 {
  private static ASTNode methodDeclarationFromString(final String ¢) {
    return make.ast(¢);
  }

  final MethodDeclaration loaded = (MethodDeclaration) methodDeclarationFromString("public void f(int x, int y, int z){ String a, b, c, d, e, f;}");
  final MethodDeclaration notLoaded = (MethodDeclaration) methodDeclarationFromString("public void g(int x, int y, int z){ String a, b, c, d;}");
  final MethodDeclaration overLoaded = (MethodDeclaration) methodDeclarationFromString(
      "public void over(Object p1, Object p2, Object p3, Object p4){Object o1, o2, o3, o4, o5, o6;}");
  final MethodDeclaration biMethod = (MethodDeclaration) methodDeclarationFromString("public void h(int x, int y){}");
  final MethodDeclaration TwoParamsFiveDefsMethod = (MethodDeclaration) methodDeclarationFromString(
      "public void h(int x, int y){int a, b, c, d, e;}");
  final MethodDeclaration loadedMethodWithLambdaDeclaration = (MethodDeclaration) methodDeclarationFromString(
      "public int foo(int x, int y, int z){String a, b, c; BiFunction<Integer> biFunc = (i1,i2) -> i1+i2;}");
  final MethodDeclaration separatedVarsDefinitionsLoadedMethod = (MethodDeclaration) methodDeclarationFromString(
      "public void bar(int x, int y, int z){String a; String b,c; Object d; boolean e;}");

  @Test public void checkIfBiMethodFailes() {
    assert !determineIf.loaded(biMethod);
  }
  @Test @SuppressWarnings("static-method") public void checkIfCompiles() {
    assert true;
  }
  @Test public void checkIfDeclInLambdaAlsoCountsPass() {
    assert determineIf.loaded(loadedMethodWithLambdaDeclaration);
  }
  @Test public void checkIfDoubledParamsAndQuintupledVarDefsFail() {
    assert !determineIf.loaded(TwoParamsFiveDefsMethod);
  }
  @Test public void checkIfLoadedMethodPasses() {
    assert determineIf.loaded(loaded);
  }
  @Test public void checkIfNotLoadedMethodFailes() {
    assert !determineIf.loaded(notLoaded);
  }
  @Test public void checkifOverLoadedMethodPass() {
    assert determineIf.loaded(overLoaded);
  }
  @Test @SuppressWarnings("static-method") public void checkIfReturnTypeIsBoolean() {
    determineIf.loaded(null);
  }
  @Test public void checkIfSeparatedVarDefAlsoCountsPass() {
    assert determineIf.loaded(separatedVarsDefinitionsLoadedMethod);
  }
  @Test public void checkIfThreeParamsPass() {
    assert determineIf.loaded(loaded);
  }
  @Test public void checkIfTripledParamsAndQuadrupleVarDefsFail() {
    assert !determineIf.loaded(notLoaded);
  }
  @Test public void checkIfTripledParamsAndQuintupledVarDefsPass() {
    assert determineIf.loaded(loaded);
  }
}
