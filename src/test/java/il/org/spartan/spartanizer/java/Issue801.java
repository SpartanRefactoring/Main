package il.org.spartan.spartanizer.java;


import static org.junit.Assert.*;
import org.junit.*;
import il.org.spartan.spartanizer.utils.Int;

/** @author David Cohen
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-9 */
 public class Issue801{
  @SuppressWarnings({ "static-method", "static-access" }) @Test public void testInt() {
    final Int x = new Int();
    x.valueOf(5);
    assertEquals(Integer.valueOf(5), x.inner());
  }

 }