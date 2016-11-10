package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
// TODO: ARIEL/ALEXANDER: TODO WARNINGS ON THIS FILE.

/** @author Alexander Kaplan
 * @author Ariel Kolikant
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue686 {
  @Test public void a() {
    assertNotNull(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo();"))));
  }
  @Test public void b() {
    assertFalse(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(String s);"))).isEmpty());
  }
  @Test public void c() {
    assertTrue(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(int s);"))).isEmpty());
  }
  @Test public void d() {
    assertEquals(2, getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(String s1, String s2);"))).size());
  }
  @Test public void e() {
    assertEquals(3, getAll.stringVariables(az.methodDeclaration(wizard.ast("public static void bar(String s1, String s2, String s3);"))).size());
  }
  @Test public void f() {
    assertEquals(3,
        getAll.stringVariables(az.methodDeclaration(wizard.ast("public static void bar(String s1, String s2, int i1, String s3);"))).size());
  }
  @Test public void g() {
    assertEquals(0, getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(int s1, int s2, int i1, int s3);"))).size());
  }
  @Test public void h() {
    assertEquals("s1", getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(String s1, int s2, int i1, int s3);"))).get(0)
        .getName().getIdentifier());
  }

  public class NotAString<T> extends ArrayList<T> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
  }

  @Test public void i() {
    assertEquals(0, getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foobar(NotAString<Integer> s1);"))).size());
  }
  @Test public void j() {
    assertEquals(0, getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foobar(NotAString<String> s1);"))).size());
  }
  @Test public void k() {
    assertEquals("s1", getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(NotAString<String> s2, String s1, int i1, int s3);")))
        .get(0).getName().getIdentifier());
  }
}
