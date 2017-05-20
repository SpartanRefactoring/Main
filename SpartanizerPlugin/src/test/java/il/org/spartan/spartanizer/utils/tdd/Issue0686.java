package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import java.util.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Alexander Kaplan
 * @author Ariel Kolikant
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0686 {
  @Test public void a() {
    assert getAll.stringVariables(az.methodDeclaration(make.ast("static void foo();"))) != null;
  }
  @Test public void b() {
    assert !getAll.stringVariables(az.methodDeclaration(make.ast("static void foo(String s);"))).isEmpty();
  }
  @Test public void c() {
    assert getAll.stringVariables(az.methodDeclaration(make.ast("static void foo(int s);"))).isEmpty();
  }
  @Test public void d() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("static void foo(String s1, String s2);"))).size(), is(2));
  }
  @Test public void e() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("public static void bar(String s1, String s2, String s3);"))).size(), is(3));
  }
  @Test public void f() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("public static void bar(String s1, String s2, int i1, String s3);"))).size(),
        is(3));
  }
  @Test public void g() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("static void bar(int s1, int s2, int i1, int s3);"))).size(), is(0));
  }
  @Test public void h() {
    azzert.that(the.firstOf(getAll.stringVariables(az.methodDeclaration(make.ast("static void bar(String s1, int s2, int i1, int s3);")))).getName()
        .getIdentifier(), is("s1"));
  }
  @Test public void i() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("static void foobar(NotAString<Integer> s1);"))).size(), is(0));
  }
  @Test public void j() {
    azzert.that(getAll.stringVariables(az.methodDeclaration(make.ast("static void foobar(NotAString<String> s1);"))).size(), is(0));
  }
  @Test public void k() {
    azzert
        .that(the.firstOf(getAll.stringVariables(az.methodDeclaration(make.ast("static void bar(NotAString<String> s2, String s1, int i1, int s3);"))))
            .getName().getIdentifier(), is("s1"));
  }

  public static class NotAString<T> extends ArrayList<T> {
    private static final long serialVersionUID = -0x24FF44898A02F0EL;
  }
}
