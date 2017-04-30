package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import nano.ly.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0763 {
  @Test public void a() {
    azzert.that(analyze.type(the.headOf(descendants.whoseClassIs(Name.class).suchThat(λ -> "x".equals(λ + "") && iz.methodInvocation(λ.getParent()))
        .from(make.ast("class C{ Map x; void foo(){ print(x);}}")))), is("Map"));
  }

  @Test public void b() {
    azzert.that(
        analyze.type(the.headOf(
            descendants.whoseClassIs(Name.class).suchThat(λ -> "x".equals(λ + "")).from(make.ast("class C{  void foo(){Map x; print(x);}}")))),
        is("Map"));
  }

  @Test public void c() {
    azzert.that(
        analyze.type(the
            .headOf(descendants.whoseClassIs(Name.class).suchThat(λ -> "x".equals(λ + "")).from(make.ast("class C{  void foo(Map x){ print(x);}}")))),
        is("Map"));
  }

  @Test public void d() {
    azzert.that(analyze.type(the.headOf(descendants.whoseClassIs(Name.class).suchThat(λ -> "x".equals(λ + ""))
        .from(make.ast("class C{  void foo(Map<String,String> x){ print(x);}}")))), is("Map<String,String>"));
  }
}
