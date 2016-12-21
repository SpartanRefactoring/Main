package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.safety.*;
import static il.org.spartan.lisp.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue763 {
  @Test public void a() {
    assertEquals("Map", analyze.type(first(searchDescendants.forClass(Name.class)
        .suchThat(x -> "x".equals(x + "") && iz.methodInvocation(x.getParent())).from(wizard.ast("class C{ Map x; void foo(){ print(x);}}")))));
  }

  @Test public void b() {
    assertEquals("Map", analyze.type(
        first(searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + "")).from(wizard.ast("class C{  void foo(){Map x; print(x);}}")))));
  }

  @Test public void c() {
    assertEquals("Map", analyze.type(
        first(searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + "")).from(wizard.ast("class C{  void foo(Map x){ print(x);}}")))));
  }

  @Test public void d() {
    assertEquals("Map<String,String>", analyze.type(first(searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + ""))
        .from(wizard.ast("class C{  void foo(Map<String,String> x){ print(x);}}")))));
  }
}
