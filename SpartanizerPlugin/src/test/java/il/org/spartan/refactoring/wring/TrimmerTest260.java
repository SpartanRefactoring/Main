package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for the nesting class Unit test for the containing class (???).
 * Note our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author matteo
 * @since 2016-08-17 */
@SuppressWarnings({ "static-method" }) public class TrimmerTest260 {
  @Test public void issue75a() {
    trimming("int i = 0").to(null);
  }

  @Test public void issue75b() {
    trimming("int i = +1;").to("int i = 1;");
  }

  @Test public void issue75c() {
    trimming("int i = +a;").to("int i = a;");
  }

  @Test public void issue75d() {
    trimming("+ 0").to("0");
  }

  @Test public void issue75e() {
    trimming("a = +0").to("a = 0");
  }

  @Test public void issue75f() {
    trimming("a = 1+0").to("a = 1");
  }

  @Test public void issue75g() {
    trimming("i=0").to(null);
  }

  @Test public void issue75h() {
    trimming("int i; i = +0;").to("int i = +0;").to("int i=0;");
  }

  @Test public void issue75i() {
    trimming("+0").to("0");
  }

  @Test public void issue75i0() {
    trimming("-+-+2").to("--+2");
  }

  @Test public void issue75i1() {
    trimming("+0").to("0");
  }

  @Test public void issue75i2() {
    trimming("+1").to("1");
  }

  @Test public void issue75i3() {
    trimming("+-1").to("-1");
  }

  @Test public void issue75i4() {
    trimming("+1.0").to("1.0");
  }

  @Test public void issue75i5() {
    trimming("+'0'").to("'0'");
  }

  @Test public void issue75i6() {
    trimming("+1L").to("1L");
  }

  @Test public void issue75i7() {
    trimming("+0F").to("0F");
  }

  @Test public void issue75i8() {
    trimming("+0L").to("0L");
  }

  @Test public void issue75j() {
    trimming("+1E3").to("1E3");
  }

  @Test public void issue75k() {
    trimming("(+(+(+x)))").to("(x)");
  }

  @Test public void issue75il() {
    trimming("+(a+b)").to("a+b");
  }

  @Test public void issue75m() {
    trimming("+ + + i").to("i");
  }

  @Test public void issue75n() {
    trimming("(2*+(a+b))").to("(2*(a+b))");
  }
}
