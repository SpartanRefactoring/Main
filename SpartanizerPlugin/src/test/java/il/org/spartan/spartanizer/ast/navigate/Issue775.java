package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue775 {
  @Test public void a() {
    assertEquals("C", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C{}")))) + "");
  }

  @Test public void b() {
    assertEquals("C<T>", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<T>{}")))) + "");
  }

  @Test public void c() {
    assertEquals("C<U,V>", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U,V>{}")))) + "");
  }

  @Test public void d() {
    assertEquals("C<U,V>", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends E,V>{}")))) + "");
  }

  @Test public void e() {
    assertEquals("C<U,V>", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends Class<V>,V>{}")))) + "");
  }

  @Test public void f() {
    assertEquals("C<U,V>", type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends Class<V<W>>,V>{}")))) + "");
  }
}
