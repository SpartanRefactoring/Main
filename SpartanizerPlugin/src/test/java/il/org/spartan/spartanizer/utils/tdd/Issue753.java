package il.org.spartan.spartanizer.utils.tdd;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

import static org.junit.Assert.*;

/** Tests of {@link enumerate.expressions}
 * @author Roei-m
 * @author RoeyMaor
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //

@SuppressWarnings("static-method") public class Issue753 {
  @Test public void a() {
    assertNull(getAll.methods(null));
  }
  
  /*
   * check that the function returns an empty list if given an empty Compilation unit
   */
  @Test public void b() {
    assert(getAll.methods(az.compilationUnit(wizard.ast("public class A {}"))).isEmpty());
  }
  
  @Test public void c() {
    assertEquals(getAll.methods(az.compilationUnit(wizard.ast("public class A {public void foo() {}}"))).size(), 1);
  }
  
}
