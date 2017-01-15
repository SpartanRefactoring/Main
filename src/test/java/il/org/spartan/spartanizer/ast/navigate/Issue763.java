package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue763 {
  @Test public void a() {
    azzert.that(analyze.type(first(searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + "") && iz.methodInvocation(x.getParent()))
        .from(wizard.ast("class C{ Map x; void foo(){ print(x);}}")))), is("Map"));
    // TODO Auto-generated method stub
  }

  @Test public void b() {
    azzert.that(
        analyze.type(first(
            searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + "")).from(wizard.ast("class C{  void foo(){Map x; print(x);}}")))),
        is("Map"));
    // TODO Auto-generated method stub
  }

  @Test public void c() {
    azzert.that(
        analyze.type(first(
            searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + "")).from(wizard.ast("class C{  void foo(Map x){ print(x);}}")))),
        is("Map"));
    // TODO Auto-generated method stub
  }

  @Test public void d() {
    azzert.that(analyze.type(first(searchDescendants.forClass(Name.class).suchThat(x -> "x".equals(x + ""))
        .from(wizard.ast("class C{  void foo(Map<String,String> x){ print(x);}}")))), is("Map<String,String>"));
    // TODO Auto-generated method stub
  }
}
