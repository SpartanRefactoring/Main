package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

/** Small fix of bloatingOf()
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-15 */
@SuppressWarnings("static-method")
public class Issue1076 {
  @Test public void a() {
    bloatingOf("").stays();
  }
}