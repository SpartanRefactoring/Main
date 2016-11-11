package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;

/** Tests of {@link Counter}
 * @author Tom Nof
 * @author Or Troyaner
 * @author Inbal Matityahu
 * @since 2016-11-11 */
@SuppressWarnings({ "static-method" }) public class Issue823 {
  @Test public void counter_test0() {
    assertNotEquals(null, new Counter());
  }
  /** Assert that the class doesn't crash when being called No way to actually
   * know the value in appearances (private field) */
  @Test public void counter_test1() {
    // TODO: Tom/Or/Inbal: I am not sure what you try to do here, but it is quite cumbersome. You can write Integer.class instead, --yg
    Integer i = Integer.valueOf(1);
    Counter.count(i.getClass());
    Counter.count(i.getClass());
  }
}
