package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #714 for more details
 * @author Dan Abramovich
 * @author Assaf Lustig
 * @author Arthur Sapozhnikov
 * @since 16-11-02 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue714 {
  @Test public void testRetTypeCompiles() {
    @SuppressWarnings("unused") final boolean b = determineIf.isImmutable(null);
  }
  @Test public void testNull() {
    auxBool(determineIf.isImmutable((TypeDeclaration) null));
  }
  @Test public void testSimpleTypeDecleration() {
    assertTrue(determineIf.isImmutable((TypeDeclaration) az.compilationUnit(wizard.ast("public class A {}")).types().get(0)));
  }
  @Test public void testNoFinal() {
    assertFalse(determineIf.isImmutable((TypeDeclaration) az.compilationUnit(wizard.ast("public class A {int x;}")).types().get(0)));
  }
  @Test public void testDoubleNotFinal() {
    assertFalse(determineIf.isImmutable(typeConvert("public class A {double x;}")));
  }
  @Test public void testONeWithFinalAndOneWithout() {
    assertFalse(determineIf.isImmutable(typeConvert("public class A {int x;final int y;}")));
  }
  @Test public void testManyFinalTypes() {
    assertTrue(determineIf.isImmutable(typeConvert("public class A {final int x; final double y; final String a; final Object o;}")));
  }
  @Test public void testManyStaticFinalTypes() {
    assertTrue(determineIf
        .isImmutable(typeConvert("public class A {final static int x; static final double y; public final String a; private final Object o;}")));
  }
  @Test public void testClassWithFUnction() {
    assertTrue(determineIf.isImmutable(typeConvert("public class A {" + "final static int x; " + "static final double y;"
        + "public void abc(int x, double y){" + "int b; final int c;" + "}" + "public class b{" + "int h;" + "}" + "final boolean g;" + "}")));
  }
  static void auxBool(@SuppressWarnings("unused") final boolean __) {
    assert true;
  }
  private TypeDeclaration typeConvert(final String $) {
    return (TypeDeclaration) az.compilationUnit(wizard.ast($)).types().get(0);
  }
}
