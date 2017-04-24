package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0075 {
  @Test public void issue075a() {
    trimminKof("int i = 0; f(++i);")//
        .stays();
  }

  @Test public void issue075b() {
    trimminKof("int i = +1; f(++i);")//
        .gives("int i = 1; f(++i);");
  }

  @Test public void issue075c() {
    trimminKof("int i = +a; f(++i);")//
        .gives("int i = a; f(++i);");
  }

  @Test public void issue075d() {
    trimminKof("+ 0")//
        .gives("0");
  }

  @Test public void issue075e() {
    trimminKof("a = +0")//
        .gives("a = 0");
  }

  @Test public void issue075f() {
    trimminKof("a = 1+0")//
        .gives("a = 1");
  }

  @Test public void issue075g() {
    trimminKof("i=0")//
        .stays();
  }

  @Test public void issue075h() {
    trimminKof("int i; i = +0;")//
        .gives("int i = +0;");
  }

  @Test public void issue075i() {
    trimminKof("+0")//
        .gives("0");
  }

  @Test public void issue075i0() {
    trimminKof("-+-+2")//
        .gives("--+2");
  }

  @Test public void issue075i1() {
    trimminKof("+0")//
        .gives("0");
  }

  @Test public void issue075i2() {
    trimminKof("+1")//
        .gives("1");
  }

  @Test public void issue075i3() {
    trimminKof("+-1")//
        .gives("-1");
  }

  @Test public void issue075i4() {
    trimminKof("+1.0")//
        .gives("1.0");
  }

  @Test public void issue075i5() {
    trimminKof("+'0'")//
        .gives("'0'");
  }

  @Test public void issue075i6() {
    trimminKof("+1L")//
        .gives("1L");
  }

  @Test public void issue075i7() {
    trimminKof("+0F")//
        .gives("0F");
  }

  @Test public void issue075i8() {
    trimminKof("+0L")//
        .gives("0L");
  }

  @Test public void issue075il() {
    trimminKof("+(a+b)")//
        .gives("a+b");
  }

  @Test public void issue075j() {
    trimminKof("+1E3")//
        .gives("1E3");
  }

  @Test public void issue075k() {
    trimminKof("(+(+(+x)))")//
        .gives("(x)");
  }

  @Test public void issue075m() {
    trimminKof("+ + + i")//
        .gives("i");
  }

  @Test public void issue075n() {
    trimminKof("(2*+(a+b))")//
        .gives("(2*(a+b))");
  }
}
