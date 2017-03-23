package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link TwoDeclarationsIntoOne}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-13 */
@SuppressWarnings("static-method")
public class Issue1012 {
  @Test public void a() {
    trimmingOf("int a = 0;int b = 1;int c = 2;f();g();")//
        .gives("int a = 0, b = 1;int c = 2;f();g();").gives("int a = 0, b = 1, c = 2;f();g();");
  }

  @Test public void b() {
    trimmingOf("int a;int b;f(a, b);")//
        .gives("int a, b;f(a, b);");
  }

  @Test public void c() {
    trimmingOf("int a;int b = 1;int c;f(a,b);g(d,c);")//
        .gives("int a, b = 1;int c;f(a,b);g(d,c);")//
        .gives("int a, b = 1, c;f(a,b);g(d,c);");
  }

  @Test public void d() {
    trimmingOf("final S[] ps={null};final S[] t=C.s(C.c(ps));f(ps);g();")//
        .gives("final S[] ps={null}, t=C.s(C.c(ps));f(ps);g();")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("final int a[] = P.r(10000);int c = 0;").stays();
  }

  @Test public void f() {
    trimmingOf("final int a = 0;int b = 8;f();g();").stays();
  }

  @Test public void g() {
    trimmingOf("final int a = 0;final int b = 8;f();g();")//
        .gives(" final int a = 0, b = 8;f();g();")//
        .stays();
  }
}
