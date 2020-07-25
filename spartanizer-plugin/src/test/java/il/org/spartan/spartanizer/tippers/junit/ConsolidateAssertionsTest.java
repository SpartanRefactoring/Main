package il.org.spartan.spartanizer.tippers.junit;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** @author Dor Ma'ayan
 * @since 2018-12-19 */
  @SuppressWarnings("static-method")
public class ConsolidateAssertionsTest {
  @Test public void test1() {
    trimmingOf("assert a; assert b;")//
        .gives("assert a && b;")//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("assert a(); assert b();")//
        .gives("assert a() && b();")//
        .stays();
  }
  // @Test public void test3() {
  // trimmingOf("assert a(); assert b(); assert c();")//
  // .gives("assert a() && b(); assert c();")//
  // .gives("assert a() && b() && c();")//
  // .stays();
  // }
}
