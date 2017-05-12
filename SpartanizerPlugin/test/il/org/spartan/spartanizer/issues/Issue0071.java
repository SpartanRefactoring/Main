package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0071 {
  @Test public void a() {
    trimmingOf("1*a")//
        .gives("a");
  }

  @Test public void b() {
    trimmingOf("a*1")//
        .gives("a");
  }

  @Test public void c() {
    trimmingOf("1*a*b")//
        .gives("a*b");
  }

  @Test public void d() {
    trimmingOf("1*a*1*b")//
        .gives("a*b");
  }

  @Test public void e() {
    trimmingOf("a*1*b*1")//
        .gives("a*b");
  }

  @Test public void f() {
    trimmingOf("1.0*a")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("a*2")//
        .gives("2*a");
  }

  @Test public void h() {
    trimmingOf("1*1")//
        .gives("1");
  }

  @Test public void i() {
    trimmingOf("1*1*1")//
        .gives("1");
  }

  @Test public void j() {
    trimmingOf("1*1*1*1*1.0")//
        .gives("1.0");
  }

  @Test public void k() {
    trimmingOf("-1*1*1")//
        .gives("-1");
  }

  @Test public void l() {
    trimmingOf("1*1*-1*-1")//
        .gives("1");
  }

  @Test public void m() {
    trimmingOf("1*1*-1*-1*-1*1*-1")//
        .gives("1");
  }

  @Test public void n() {
    trimmingOf("1*1")//
        .gives("1");
  }

  @Test public void o() {
    trimmingOf("(1)*((a))")//
        .gives("a");
  }

  @Test public void p() {
    trimmingOf("((1)*((a)))")//
        .gives("(a)");
  }

  @Test public void q() {
    trimmingOf("1L*1")//
        .gives("1L");
  }

  @Test public void r() {
    trimmingOf("1L*a")//
        .stays();
  }
}
