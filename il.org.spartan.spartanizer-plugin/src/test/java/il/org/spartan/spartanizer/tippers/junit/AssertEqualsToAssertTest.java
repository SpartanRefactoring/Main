package il.org.spartan.spartanizer.tippers.junit;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.java.*;

/**
 * @author Dor Ma'ayan
 * @since 2018-12-19
 * */
public class AssertEqualsToAssertTest {
  @Test public void test1() {
    trimmingOf("assertEquals(a(),b())")//
        .gives("assert a().equals(b());")//
        .stays();
  }
  
  @Test public void test2() {
    trimmingOf("assertEquals(1,b())")//
        .gives("assert 1 == b();");
  }
  
  @Test public void test3() {
    trimmingOf("int a; assertEquals(a,b());")//
        .gives("int a; assert a == b();;");
  }
  
  @Test public void test4() {
    trimmingOf("int a; assertEquals(m,a,b());")//
        .gives("int a; assert a == b() : m;;");
  }
  
  @Test public void test5() {
    trimmingOf("assertEquals(m,a,b());")//
        .gives("assert a.equals(b()) : m;;");
  }
    
}
