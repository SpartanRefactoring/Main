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
    
}
