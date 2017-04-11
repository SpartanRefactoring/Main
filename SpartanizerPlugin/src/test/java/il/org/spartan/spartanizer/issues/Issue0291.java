package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Check that the basic cases of arithmetic evaluation combined with parameters
 * are working well
 * @author Dor Ma'ayan
 * @since 18-11-2016 */
@SuppressWarnings("static-method")
public class Issue0291 {
  @Test public void test00() {
    topDownTrimming("a+2==3")//
        .gives("a==3-2")//
        .gives("a==1")//
        .stays();
  }

  @Test public void test01() {
    topDownTrimming("3==a+2")//
        .gives("a+2==3")//
        .gives("a==3-2")//
        .gives("a==1")//
        .stays();
  }

  @Test public void test02() {
    topDownTrimming("a+5==b+2")//
        .gives("a==b+2-5");
  }

  @Test public void test03() {
    topDownTrimming("a+2.2==3.89")//
        .gives("a==3.89-2.2")//
        .gives("a==1.69")//
        .stays();
  }

  @Test public void test04() {
    topDownTrimming("a+2.2==b")//
        .gives("a==b-2.2")//
        .stays();
  }

  @Test public void test05() {
    topDownTrimming("a+22+4==b")//
        .gives("a+26==b")//
        .gives("a==b-26")//
        .stays();
  }

  @Test public void test06() {
    topDownTrimming("a-22==b")//
        .gives("a==b+22")//
        .stays();
  }

  @Test public void test07() {
    topDownTrimming("a-2.2==3.89")//
        .gives("a==3.89+2.2")//
        .gives("a==6.09")//
        .stays();
  }

  @Test public void test08() {
    topDownTrimming("a-22==b+c+d")//
        .gives("a==b+c+d+22")//
        .stays();
  }

  @Test public void test09() {
    topDownTrimming("a< b+1")//
        .gives("a<=b")//
        .stays();
  }

  @Test public void test10() {
    topDownTrimming("a+2<length")//
        .gives("a<length-2")//
        .stays();
  }

  @Test public void test11() {
    topDownTrimming("a-2<length+9")//
        .gives("a<length+9+2")//
        .gives("a<length+11")//
        .stays();
  }

  @Test public void test12() {
    topDownTrimming("a-2>length+9")//
        .gives("a>length+9+2")//
        .gives("a>length+11")//
        .stays();
  }

  @Test public void test13() {
    topDownTrimming("a+2>length")//
        .gives("a>length-2")//
        .stays();
  }

  @Test public void test14() {
    topDownTrimming("a -b > c - d")//
        .gives("a + d > c + b");
  }

  @Test public void test15() {
    topDownTrimming("a -b == c - d")//
        .gives("a + d == c + b");
  }

  @Test public void test16() {
    topDownTrimming("a -b < c - d")//
        .gives("a + d < c + b");
  }

  @Test public void test17() {
    topDownTrimming("a -b >= c - d")//
        .gives("a + d >= c + b")//
        .gives("a + d >= b + c")//
        .stays();
  }

  @Test public void test18() {
    topDownTrimming("a -b - f < c - d")//
        .stays();
  }

  @Test public void test19() {
    topDownTrimming("a -(b - f) < (c - d) -t")//
        .gives("a +t < c - d + b - f");
  }

  @Test public void test20() {
    topDownTrimming("a+1 <= length")//
        .gives("a <= length-1")//
        .gives("a<length");
  }

  @Test public void test21() {
    topDownTrimming("a-1 <= length")//
        .gives("a <= length+1");
  }

  /** Infinite Loop test check Issue #1021 */
  @Test public void test22() {
    topDownTrimming(" public class C {public  void foo() {t(A + 50 < B);}}")//
        .stays();
  }
}
