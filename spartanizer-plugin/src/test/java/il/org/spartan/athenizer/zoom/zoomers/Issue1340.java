package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

import il.org.spartan.athenizer.zoomers.MultiplicationToCast;

/** Bug in {@link MultiplicationToCast} test case
 * @author Yuval Simon
 * @since 2017-05-12 */
@SuppressWarnings("static-method")
public class Issue1340 {
  @Test public void t1() {
    bloatingOf("Long a; a = k * 0xff51afd7ed558ccdL;").stays();
  }
}
