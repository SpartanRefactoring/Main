package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Shimon Azulay & Idan Atias & Lior Ben Ami
 * @since 16-11-3 */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue741 {
  @Test public void publicFields_test0() {
    getAll2.publicFields(null);
    assert true;
  }

  @SuppressWarnings("cast") @Test public void publicFields_test1() {
    assertTrue(getAll2.publicFields(null) instanceof List<?>);
  }

  @Test public void publicFields_test2() {
    assertNotNull(getAll2.publicFields(null));
  }

  @Test public void publicFields_test3() {
    assertEquals(0, getAll2.publicFields(null).size());
  }

  @Test public void publicFields_test4() {
    TypeDeclaration td = (TypeDeclaration) az.compilationUnit(wizard.ast("public class A {}")).types().get(0);
    assertEquals(0, getAll2.publicFields(td).size());
  }
  
  @Test public void publicFields_test5() {
    TypeDeclaration td = (TypeDeclaration) az.compilationUnit(wizard.ast("public class A { private int x; protected String s; }")).types().get(0);
    assertEquals(0, getAll2.publicFields(td).size());
  }
  
  
}
