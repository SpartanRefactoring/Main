package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
// TODO: ARIEL/ALEXANDER: TODO WARNINGS ON THIS FILE.

/** TODO: Alexander Kaplan please add a description
 * @author Alexander Kaplan
 * @author Ariel Kolikant
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0686 {
  public class NotAString<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1L;
  }

  @Test public void a() {
    assert getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo();"))) != null;
  }

  @Test public void b() {
    assert !getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(String s);"))).isEmpty();
  }

  @Test public void c() {
    assert getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(int s);"))).isEmpty();
  }

  @Test public void d() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foo(String s1, String s2);"))).size(), is(2));
  }

  @Test public void e() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("public static void bar(String s1, String s2, String s3);"))).size(), is(3));
  }

  @Test public void f() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("public static void bar(String s1, String s2, int i1, String s3);"))).size(),
        is(3));
  }

  @Test public void g() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(int s1, int s2, int i1, int s3);"))).size(), is(0));
  }

  @Test public void h() {
    azzert.that(first(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(String s1, int s2, int i1, int s3);")))).getName()
        .getIdentifier(), is("s1"));
  }

  @Test public void i() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foobar(NotAString<Integer> s1);"))).size(), is(0));
  }

  @Test public void j() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void foobar(NotAString<String> s1);"))).size(), is(0));
  }

  @Test public void k() {
    azzert.that(first(getAll.stringVariables(az.methodDeclaration(wizard.ast("static void bar(NotAString<String> s2, String s1, int i1, int s3);"))))
        .getName().getIdentifier(), is("s1"));
  }
}
