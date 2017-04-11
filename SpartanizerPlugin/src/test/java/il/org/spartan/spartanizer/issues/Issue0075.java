package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0075 {
  @Test public void issue075a() {
    topDownTrimming("int i = 0; f(++i);")//
        .stays();
  }

  @Test public void issue075b() {
    topDownTrimming("int i = +1; f(++i);")//
        .gives("int i = 1; f(++i);");
  }

  @Test public void issue075c() {
    topDownTrimming("int i = +a; f(++i);")//
        .gives("int i = a; f(++i);");
  }

  @Test public void issue075d() {
    topDownTrimming("+ 0")//
        .gives("0");
  }

  @Test public void issue075e() {
    topDownTrimming("a = +0")//
        .gives("a = 0");
  }

  @Test public void issue075f() {
    topDownTrimming("a = 1+0")//
        .gives("a = 1");
  }

  @Test public void issue075g() {
    topDownTrimming("i=0")//
        .stays();
  }

  @Test public void issue075h() {
    topDownTrimming("int i; i = +0;")//
        .gives("int i = +0;");
  }

  @Test public void issue075i() {
    topDownTrimming("+0")//
        .gives("0");
  }

  @Test public void issue075i0() {
    topDownTrimming("-+-+2")//
        .gives("--+2");
  }

  @Test public void issue075i1() {
    topDownTrimming("+0")//
        .gives("0");
  }

  @Test public void issue075i2() {
    topDownTrimming("+1")//
        .gives("1");
  }

  @Test public void issue075i3() {
    topDownTrimming("+-1")//
        .gives("-1");
  }

  @Test public void issue075i4() {
    topDownTrimming("+1.0")//
        .gives("1.0");
  }

  @Test public void issue075i5() {
    topDownTrimming("+'0'")//
        .gives("'0'");
  }

  @Test public void issue075i6() {
    topDownTrimming("+1L")//
        .gives("1L");
  }

  @Test public void issue075i7() {
    topDownTrimming("+0F")//
        .gives("0F");
  }

  @Test public void issue075i8() {
    topDownTrimming("+0L")//
        .gives("0L");
  }

  @Test public void issue075il() {
    topDownTrimming("+(a+b)")//
        .gives("a+b");
  }

  @Test public void issue075j() {
    topDownTrimming("+1E3")//
        .gives("1E3");
  }

  @Test public void issue075k() {
    topDownTrimming("(+(+(+x)))")//
        .gives("(x)");
  }

  @Test public void issue075m() {
    topDownTrimming("+ + + i")//
        .gives("i");
  }

  @Test public void issue075n() {
    topDownTrimming("(2*+(a+b))")//
        .gives("(2*(a+b))");
  }
}
