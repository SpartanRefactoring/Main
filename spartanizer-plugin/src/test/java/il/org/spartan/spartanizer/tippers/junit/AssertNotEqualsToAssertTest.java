package il.org.spartan.spartanizer.tippers.junit;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/**
 * @author Dor Ma'ayan
 * @since 2018-12-19
 */
@SuppressWarnings("static-method") public class AssertNotEqualsToAssertTest {
  @Test public void test1() {
    trimmingOf("assertNotEquals(a(),b())")//
        .gives("assert !a().equals(b());")//
        .stays();
  }

  @Test public void test2() {
    trimmingOf("assertNotEquals(1,b())")//
        .gives("assert 1 != b();");
  }

  @Test public void test3() {
    trimmingOf("int a; assertNotEquals(a,b());")//
        .gives("int a; assert a != b();;");
  }

  @Test public void test4() {
    trimmingOf("int a; assertNotEquals(m,a,b());")//
        .gives("int a; assert a != b() : m;;");
  }

  @Test public void test5() {
    trimmingOf("assertNotEquals(m,a,b());")//
        .gives("assert !a.equals(b()) : m;;");
  }

  @Test public void test6() {
    trimmingOf("assertNotSame(a(),b())")//
        .gives("assert a() != b();")//
        .stays();
  }

  @Test public void test7() {
    trimmingOf("assertNotSame(1,b())")//
        .gives("assert 1 != b();");
  }

  @Test public void test8() {
    trimmingOf("int a; assertNotSame(a,b());")//
        .gives("int a; assert a != b();;");
  }

  @Test public void test9() {
    trimmingOf("int a; assertNotSame(m,a,b());")//
        .gives("int a; assert a != b() : m;;");
  }

  @Test public void test10() {
    trimmingOf("assertNotSame(m,a,b());")//
        .gives("assert a != b() : m;;");
  }

}
