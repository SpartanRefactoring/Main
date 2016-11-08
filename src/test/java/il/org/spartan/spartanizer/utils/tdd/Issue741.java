package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Shimon Azulay
 *  @author Idan Atias
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
    assertEquals(0, getAll2.publicFields(getTypeDeclaration("public class A {}")).size());
  }

  @Test public void publicFields_test5() {
    assertEquals(0, getAll2.publicFields(getTypeDeclaration("public class A { private int x; protected String s; }")).size());
  }

  @Test public void publicFields_test6() {
    TypeDeclaration td = getTypeDeclaration("public class A { private int x; public static char y; public static void f(){}}");
    List<String> pFields = getAll2.publicFields(td);
    assertTrue(pFields.contains("y"));
    assertFalse(pFields.contains("x"));
    assertFalse(pFields.contains("f"));
  }

  @Test public void publicFields_test7() {
    TypeDeclaration td = getTypeDeclaration("public class A { public int x; class B { public int y; } }");
    List<String> pFields = getAll2.publicFields(td);
    assertTrue(pFields.contains("x"));
    assertFalse(pFields.contains("y"));
  }

  @Test public void publicFields_test8() {
    TypeDeclaration td = getTypeDeclaration("public class A { public class B { public int x; } }");
    List<String> pFields = getAll2.publicFields(td);
    assertFalse(pFields.contains("B"));
    assertFalse(pFields.contains("x"));
  }

  static TypeDeclaration getTypeDeclaration(String td) {
    return findFirst.typeDeclaration(wizard.ast(td));
  }
}
