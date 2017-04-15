package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0108 {
  @Test public void a() {
    trimminKof("x=x*y")//
        .gives("x*=y");
  }

  @Test public void b() {
    trimminKof("x=y*x")//
        .gives("x*=y");
  }

  @Test public void c() {
    trimminKof("x=y*z")//
        .stays();
  }

  @Test public void d() {
    trimminKof("x = x * x")//
        .gives("x*=x");
  }

  @Test public void e() {
    trimminKof("x = y * z * x * k * 9")//
        .gives("x *= y * z * k * 9");
  }

  @Test public void f() {
    trimminKof("a = y * z * a")//
        .gives("a *= y * z");
  }

  @Test public void g() {
    trimminKof("a=a*5")//
        .gives("a*=5");
  }

  @Test public void h() {
    trimminKof("a=a*(alex)")//
        .gives("a*=alex");
  }

  @Test public void i() {
    trimminKof("a = a * (c = c * kif)")//
        .gives("a *= c = c*kif")//
        .gives("a *= c *= kif")//
        .stays();
  }

  @Test public void j() {
    trimminKof("x=x*foo(x,y)")//
        .gives("x*=foo(x,y)");
  }

  @Test public void k() {
    trimminKof("z=foo(x=(y=y*u),17)")//
        .gives("z=foo(x=(y*=u),17)");
  }
}
