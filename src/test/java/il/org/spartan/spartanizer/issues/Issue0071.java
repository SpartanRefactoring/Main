package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0071 {
  @Test public void a() {
    topDownTrimming("1*a")//
        .gives("a");
  }

  @Test public void b() {
    topDownTrimming("a*1")//
        .gives("a");
  }

  @Test public void c() {
    topDownTrimming("1*a*b")//
        .gives("a*b");
  }

  @Test public void d() {
    topDownTrimming("1*a*1*b")//
        .gives("a*b");
  }

  @Test public void e() {
    topDownTrimming("a*1*b*1")//
        .gives("a*b");
  }

  @Test public void f() {
    topDownTrimming("1.0*a")//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("a*2")//
        .gives("2*a");
  }

  @Test public void h() {
    topDownTrimming("1*1")//
        .gives("1");
  }

  @Test public void i() {
    topDownTrimming("1*1*1")//
        .gives("1");
  }

  @Test public void j() {
    topDownTrimming("1*1*1*1*1.0")//
        .gives("1.0");
  }

  @Test public void k() {
    topDownTrimming("-1*1*1")//
        .gives("-1");
  }

  @Test public void l() {
    topDownTrimming("1*1*-1*-1")//
        .gives("1");
  }

  @Test public void m() {
    topDownTrimming("1*1*-1*-1*-1*1*-1")//
        .gives("1");
  }

  @Test public void n() {
    topDownTrimming("1*1")//
        .gives("1");
  }

  @Test public void o() {
    topDownTrimming("(1)*((a))")//
        .gives("a");
  }

  @Test public void p() {
    topDownTrimming("((1)*((a)))")//
        .gives("(a)");
  }

  @Test public void q() {
    topDownTrimming("1L*1")//
        .gives("1L");
  }

  @Test public void r() {
    topDownTrimming("1L*a")//
        .stays();
  }
}
