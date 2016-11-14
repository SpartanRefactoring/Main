package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;

/** @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 **/
@SuppressWarnings({ "static-method", })
// TODO: David/Shahar/Zahi: your tests could have been more extensive --yg
public class Issue801 {
  @Test public void test01() {
    assertEquals(Integer.valueOf(5), Int.valueOf(5).inner());
  }

  @Test public void test02() {
    assertEquals(Integer.valueOf(0), Int.valueOf(0).inner());
  }
}