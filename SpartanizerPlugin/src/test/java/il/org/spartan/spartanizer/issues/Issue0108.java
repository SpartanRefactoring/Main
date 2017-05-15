package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0108 {
  @Test public void a() {
    trimmingOf("x=x*y")//
        .gives("x*=y");
  }
  @Test public void b() {
    trimmingOf("x=y*x")//
        .gives("x*=y");
  }
  @Test public void c() {
    trimmingOf("x=y*z")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("x = x * x")//
        .gives("x*=x");
  }
  @Test public void e() {
    trimmingOf("x = y * z * x * k * 9")//
        .gives("x *= y * z * k * 9");
  }
  @Test public void f() {
    trimmingOf("a = y * z * a")//
        .gives("a *= y * z");
  }
  @Test public void g() {
    trimmingOf("a=a*5")//
        .gives("a*=5");
  }
  @Test public void h() {
    trimmingOf("a=a*(alex)")//
        .gives("a*=alex");
  }
  @Test public void i() {
    trimmingOf("a = a * (c = c * kif)")//
        .gives("a *= c = c*kif")//
        .gives("a *= c *= kif")//
        .stays();
  }
  @Test public void j() {
    trimmingOf("x=x*foo(x,y)")//
        .gives("x*=foo(x,y)");
  }
  @Test public void k() {
    trimmingOf("z=foo(x=(y=y*u),17)")//
        .gives("z=foo(x=(y*=u),17)");
  }
}
