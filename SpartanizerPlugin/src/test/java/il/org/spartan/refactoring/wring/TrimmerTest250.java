package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static il.org.spartan.utils.Utils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;

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

}
