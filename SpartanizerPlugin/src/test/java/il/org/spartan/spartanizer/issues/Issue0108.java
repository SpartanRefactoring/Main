package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0108 {
  @Test public void a() {
    topDownTrimming("x=x*y")//
        .gives("x*=y");
  }

  @Test public void b() {
    topDownTrimming("x=y*x")//
        .gives("x*=y");
  }

  @Test public void c() {
    topDownTrimming("x=y*z")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("x = x * x")//
        .gives("x*=x");
  }

  @Test public void e() {
    topDownTrimming("x = y * z * x * k * 9")//
        .gives("x *= y * z * k * 9");
  }

  @Test public void f() {
    topDownTrimming("a = y * z * a")//
        .gives("a *= y * z");
  }

  @Test public void g() {
    topDownTrimming("a=a*5")//
        .gives("a*=5");
  }

  @Test public void h() {
    topDownTrimming("a=a*(alex)")//
        .gives("a*=alex");
  }

  @Test public void i() {
    topDownTrimming("a = a * (c = c * kif)")//
        .gives("a *= c = c*kif")//
        .gives("a *= c *= kif")//
        .stays();
  }

  @Test public void j() {
    topDownTrimming("x=x*foo(x,y)")//
        .gives("x*=foo(x,y)");
  }

  @Test public void k() {
    topDownTrimming("z=foo(x=(y=y*u),17)")//
        .gives("z=foo(x=(y*=u),17)");
  }
}
