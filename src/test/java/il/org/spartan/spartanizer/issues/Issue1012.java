package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link TwoDeclarationsIntoOne}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-13 */
@SuppressWarnings("static-method")
public class Issue1012 {
  @Test public void a() {
    trimmingOf("int a = q();int b = z();int c =rs();f();g(); return a + b +c;")//
        .gives("int a = q(), b = z();int c =rs();f();g(); return a + b +c;")//
        .gives("int a = q(), b = z(), c =rs();f();g(); return a + b +c;");
  }

  @Test public void b() {
    trimmingOf("int a;int b;f(a, b); return a + b +c;")//
        .gives("int a, b;f(a, b); return a + b +c;");
  }

  @Test public void c() {
    trimmingOf("int a;int b = z();int c;f(a,b);g(d,c);")//
        .gives("int a, b = z();int c;f(a,b);g(d,c);")//
        .gives("int a, b = z(), c;f(a,b);g(d,c);");
  }

  @Test public void d() {
    trimmingOf("final S[] ps={null};final S[] t=C.s(C.c(ps));f(ps);g();")//
        .gives("final S[] ps={null}, t=C.s(C.c(ps));f(ps);g();")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("final int a[] = P.r(10000);int c = q();") //
        .stays();
  }

  @Test public void f() {
    trimmingOf("final int a = q();int b = e();f();g(); return a+b;")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("final int a = q();final int b = e();f();g(); return a+b;")//
        .gives(" final int a = q(), b = e();f();g(); return a+b;")//
        .stays();
  }
}
