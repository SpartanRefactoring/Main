package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0071 {
  @Test public void a() {
    trimminKof("1*a")//
        .gives("a");
  }

  @Test public void b() {
    trimminKof("a*1")//
        .gives("a");
  }

  @Test public void c() {
    trimminKof("1*a*b")//
        .gives("a*b");
  }

  @Test public void d() {
    trimminKof("1*a*1*b")//
        .gives("a*b");
  }

  @Test public void e() {
    trimminKof("a*1*b*1")//
        .gives("a*b");
  }

  @Test public void f() {
    trimminKof("1.0*a")//
        .stays();
  }

  @Test public void g() {
    trimminKof("a*2")//
        .gives("2*a");
  }

  @Test public void h() {
    trimminKof("1*1")//
        .gives("1");
  }

  @Test public void i() {
    trimminKof("1*1*1")//
        .gives("1");
  }

  @Test public void j() {
    trimminKof("1*1*1*1*1.0")//
        .gives("1.0");
  }

  @Test public void k() {
    trimminKof("-1*1*1")//
        .gives("-1");
  }

  @Test public void l() {
    trimminKof("1*1*-1*-1")//
        .gives("1");
  }

  @Test public void m() {
    trimminKof("1*1*-1*-1*-1*1*-1")//
        .gives("1");
  }

  @Test public void n() {
    trimminKof("1*1")//
        .gives("1");
  }

  @Test public void o() {
    trimminKof("(1)*((a))")//
        .gives("a");
  }

  @Test public void p() {
    trimminKof("((1)*((a)))")//
        .gives("(a)");
  }

  @Test public void q() {
    trimminKof("1L*1")//
        .gives("1L");
  }

  @Test public void r() {
    trimminKof("1L*a")//
        .stays();
  }
}
