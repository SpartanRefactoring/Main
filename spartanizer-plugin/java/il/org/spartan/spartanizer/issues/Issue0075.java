package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0075 {
  @Test public void issue075a() {
    trimmingOf("int i = 0; f(++i);")//
        .stays();
  }
  @Test public void issue075b() {
    trimmingOf("int i = +1; f(++i);")//
        .gives("int i = 1; f(++i);");
  }
  @Test public void issue075c() {
    trimmingOf("int i = +a; f(++i);")//
        .gives("int i = a; f(++i);");
  }
  @Test public void issue075d() {
    trimmingOf("+ 0")//
        .gives("0");
  }
  @Test public void issue075e() {
    trimmingOf("a = +0")//
        .gives("a = 0");
  }
  @Test public void issue075f() {
    trimmingOf("a = 1+0")//
        .gives("a = 1");
  }
  @Test public void issue075g() {
    trimmingOf("i=0")//
        .stays();
  }
  @Test public void issue075h() {
    trimmingOf("int i; i = +0;")//
        .gives("int i = +0;");
  }
  @Test public void issue075i() {
    trimmingOf("+0")//
        .gives("0");
  }
  @Test public void issue075i0() {
    trimmingOf("-+-+2")//
        .gives("--+2");
  }
  @Test public void issue075i1() {
    trimmingOf("+0")//
        .gives("0");
  }
  @Test public void issue075i2() {
    trimmingOf("+1")//
        .gives("1");
  }
  @Test public void issue075i3() {
    trimmingOf("+-1")//
        .gives("-1");
  }
  @Test public void issue075i4() {
    trimmingOf("+1.0")//
        .gives("1.0");
  }
  @Test public void issue075i5() {
    trimmingOf("+'0'")//
        .gives("'0'");
  }
  @Test public void issue075i6() {
    trimmingOf("+1L")//
        .gives("1L");
  }
  @Test public void issue075i7() {
    trimmingOf("+0F")//
        .gives("0F");
  }
  @Test public void issue075i8() {
    trimmingOf("+0L")//
        .gives("0L");
  }
  @Test public void issue075il() {
    trimmingOf("+(a+b)")//
        .gives("a+b");
  }
  @Test public void issue075j() {
    trimmingOf("+1E3")//
        .gives("1E3");
  }
  @Test public void issue075k() {
    trimmingOf("(+(+(+x)))")//
        .gives("(x)");
  }
  @Test public void issue075m() {
    trimmingOf("+ + + i")//
        .gives("i");
  }
  @Test public void issue075n() {
    trimmingOf("(2*+(a+b))")//
        .gives("(2*(a+b))");
  }
}
