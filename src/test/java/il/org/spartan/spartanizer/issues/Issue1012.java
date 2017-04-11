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
    topDownTrimming("int a = 0;int b = 1;int c = 2;f(a,b,c);g(c,b,a);")//
        .gives("int a = 0, b = 1;int c = 2;f(a,b,c);g(c,b,a);")//
        .gives("int a = 0, b = 1, c = 2;f(a,b,c);g(c,b,a);");
  }

  @Test public void b() {
    topDownTrimming("int a;int b;f(a, b);")//
        .gives("int a, b;f(a, b);");
  }

  @Test public void c() {
    topDownTrimming("int a;int b = 1;int c;f(a,b);g(d,c);")//
        .gives("int a, b = 1;int c;f(a,b);g(d,c);")//
        .gives("int a, b = 1, c;f(a,b);g(d,c);");
  }

  @Test public void d() {
    topDownTrimming("final S[] ps={null};final S[] t=C.s(C.c(ps));f(ps);g(ps,tc,t,p);")//
        .gives("final S[] ps={null}, t=C.s(C.c(ps));f(ps);g(ps,tc,t,p);")//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("final int a[] = P.r(10000);int c = 0;f(c,a,c,a,c);")//
        .gives("final int a[] = P.r(10000);f(0,a,0,a,0);")//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("final int a = 0;int b = 8;f(a,b,a,a);g(a,b,a,a);")//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("final int a = 0;final int b = 8;f(a,b,a,a,b);g(a,b,a,a,b);")//
        .gives("final int a = 0, b = 8;f(a,b,a,a,b);g(a,b,a,a,b);")//
        .stays();
  }
}
