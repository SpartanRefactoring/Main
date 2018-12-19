package il.org.spartan.spartanizer.tippers.junit;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO dormaayn: document class 
 * 
 * @author dormaayn
 * @since 2018-12-19 */
public class AssertTrueFalseToAssertTest {
  @Test public void test1() {
    trimmingOf("assertTrue(a() == b());")//
        .gives("assert a() == b();")//
        .stays();
  }
  
  @Test public void test2() {
    trimmingOf("assertFalse(a() == b());")//
        .gives("assert a() != b();")//
        .stays();
  }
  
  @Test public void test3() {
    trimmingOf("assertFalse(a());")//
        .gives("assert !a();")//
        .stays();
  }
  
}
