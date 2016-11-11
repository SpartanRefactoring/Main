package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;

/** Tests of {@link utils.Counter}
 * @author Tom Nof
 * @author Or Troyaner
 * @author Inbal Matityahu
 * @since 16-11-11 */
@SuppressWarnings({ "static-method" }) public class Issue823 {
  @Test public void counter_test0() {
    assertNotEquals(null, new Counter());
  }
  @Test public void counter_test1() {
    // Assert that the class doesn't crash when being called
    // No way to actually know the value in appearances (private field)
    Integer i = Integer.valueOf(1);
    Counter.count(i.getClass());
    Counter.count(i.getClass());
  }
}
