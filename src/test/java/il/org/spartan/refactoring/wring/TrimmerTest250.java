package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class TrimmerTest250 {
  @Test public void issue70_01() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_02() {
    trimming("(double)4").to("1.*4");
  }

  @Test public void issue70_03() {
    trimming("(double)1.2").to("1.*1.2");
  }

  @Test public void issue70_04() {
    trimming("(double)'a'").to("1.*'a'");
  }

  @Test public void issue70_05() {
    trimming("(double)A").to("1.*A");
  }

  @Test public void issue70_06() {
    trimming("(double)a.b").to("1.*a.b");
  }

  @Test public void issue70_07() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_08() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_09() {
    trimming("(double) 2. * (double)5")//
        .to("(double)5 * (double)2.") //
        .to("1. * 5  * 1. * 2.")//
        .to("");
  }

  @Test public void issue70_10() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_11() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_12() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_13() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_14() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_15() {
    trimming("(double)5").to("1.*5");
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_16() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_17() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_18() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue70_19() {
    trimming("(double)5").to("1.*5");
  }

  @Test public void issue71a() {
    trimming("1*a").to("a");
  }

  @Test public void issue71b() {
    trimming("a*1").to("a");
  }

  @Test public void issue71c() {
    trimming("1*a*b").to("a*b");
  }

  @Test public void issue71d() {
    trimming("1*a*1*b").to("a*b");
  }

  @Test public void issue71e() {
    trimming("a*1*b*1").to("a*b");
  }

  @Test public void issue71f() {
    trimming("1.0*a").to(null);
  }

  @Test public void issue71g() {
    trimming("a*2").to("2*a");
  }

  @Test public void issue71h() {
    trimming("1*1").to("1");
  }

  @Test public void issue71i() {
    trimming("1*1*1").to("1");
  }

  @Test public void issue71j() {
    trimming("1*1*1*1*1.0").to("1.0");
  }

  @Test public void issue71k() {
    trimming("-1*1*1").to("-1");
  }

  @Test public void issue71l() {
    trimming("1*1*-1*-1").to("1*1*1*1").to("1");
  }

  @Test public void issue71m() {
    trimming("1*1*-1*-1*-1*1*-1").to("1*1*1*1*1*1*1").to("1");
  }

  @Test public void issue71n() {
    trimming("1*1").to("1");
  }

  @Test public void issue71o() {
    trimming("(1)*((a))").to("a");
  }

  @Test public void issue71p() {
    trimming("((1)*((a)))").to("(a)");
  }

  @Test public void issue71q() {
    trimming("1L*1").to("1L");
  }

  @Test public void issue71r() {
    trimming("1L*a").to("");
  }

  @Test public void issue72a() {
    trimming("x+0").to("x");
  }

  @Test public void issue72b() {
    trimming("0+x").to("x");
  }

  @Test public void issue82a() {
    trimming("(long)5").to("1L*5");
  }

  @Test public void issue82b() {
    trimming("(long)a").to("1L*a");
  }

  @Test public void issue82c() {
    trimming("(long)(long)a").to("1L*(long)a").to("1L*1L*a");
  }

  @Test public void issue82d() {
    trimming("(long)a*(long)b").to("1L*a*1L*b");
  }

  @Test public void issue82e() {
    trimming("(double)(long)a").to("1.*(long)a").to("1.*1L*a");
  }
}
