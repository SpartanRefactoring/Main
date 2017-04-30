package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import nano.ly.*;

/** see issue #714 for more details
 * @author Dan Abramovich
 * @author Assaf Lustig
 * @author Arthur Sapozhnikov
 * @since 16-11-02 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0714 {
  static void auxBool(@SuppressWarnings("unused") final boolean __) {
    assert true;
  }

  @Test public void testClassWithFUnction() {
    assert determineIf.isImmutable(typeConvert("public class A {final static int x; static final double y;"
        + "public void abc(int x, double y){int b; final int c;}public class b{int h;}final boolean g;}"));
  }

  @Test public void testDoubleNotFinal() {
    assert !determineIf.isImmutable(typeConvert("public class A {double x;}"));
  }

  @Test public void testManyFinalTypes() {
    assert determineIf.isImmutable(typeConvert("public class A {final int x; final double y; final String a; final Object o;}"));
  }

  @Test public void testManyStaticFinalTypes() {
    assert determineIf
        .isImmutable(typeConvert("public class A {final static int x; static final double y; public final String a; private final Object o;}"));
  }

  @Test public void testNoFinal() {
    assert !determineIf.isImmutable((TypeDeclaration) the.headOf(types(az.compilationUnit(make.ast("public class A {int x;}")))));
  }

  @Test public void testNull() {
    auxBool(determineIf.isImmutable(null));
  }

  @Test public void testONeWithFinalAndOneWithout() {
    assert !determineIf.isImmutable(typeConvert("public class A {int x;final int y;}"));
  }

  @Test public void testRetTypeCompiles() {
    (determineIf.isImmutable(null) + "").hashCode();
  }

  @Test public void testSimpleTypeDecleration() {
    assert determineIf.isImmutable((TypeDeclaration) the.headOf(types(az.compilationUnit(make.ast("public class A {}")))));
  }

  private TypeDeclaration typeConvert(final String $) {
    return (TypeDeclaration) the.headOf(types(az.compilationUnit(make.ast($))));
  }
}
