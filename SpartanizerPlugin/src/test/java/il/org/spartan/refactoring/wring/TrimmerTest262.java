package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for the nesting class Unit test for the containing class (???).
 * Note our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author matteo
 * @since 2016-08-17 */

@SuppressWarnings({ "static-method" }) public class TrimmerTest262 {
  @Test public void issue76a() {
    trimming("a*b + a*c").to("a*(b+c)");
  }
  
  @Test public void issue76b() {
    trimming("b*a + c*a").to("a*(b+c)");
  }
  
  @Test public void issue76c() {
    trimming("b*a + c*a + d*a").to("a*(b+c+d)");
  }
  
  @Test public void issue76d() {
    trimming("h*g + c*a + d*a").to("h*g + a*(c+d)");
  }
  
  @Test public void issue76e() {
    trimming("(f+g)*a + c*a + d*a").to("a*(f+g+c+d)");
  }
  
  @Test public void issue76f() {
    trimming("b*b + c*a + d*a").to("b*b + a*(c+d)");
  }
  
  @Test public void issue76g() {
    trimming("a*a + c*a + d*a").to("a*(a+c+d)");
  }
  
  @Test public void issue76h() {
    trimming("a*h*b + a*h*c + a*h*d").to("a*h*(b+c+d)");
  }
  
  @Test public void issue76i() {
    trimming("a*b + c*d + e*f").to("a*b + c*d + e*f");
  }

  
  
}
