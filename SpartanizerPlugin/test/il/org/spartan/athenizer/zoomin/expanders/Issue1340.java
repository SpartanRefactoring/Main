package il.org.spartan.athenizer.zoomin.expanders;

import org.junit.*;
import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

/** Bug in {@link MultiplicationToCast} test case
 * @author Yuval Simon
 * @since 2017-05-12 */
@Ignore
@SuppressWarnings("static-method")
public class Issue1340 {
  @Test public void t1() {
    bloatingOf("Long a; a = k * 0xff51afd7ed558ccdL;").stays();
  }
}
