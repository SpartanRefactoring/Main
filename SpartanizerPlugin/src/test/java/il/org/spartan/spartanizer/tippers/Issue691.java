package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.utils.tdd.*;

/** @author Dor Ma'ayan
 * @author Omri Shmuel
 * @since 01-11-2016 */
public class Issue691 {
  @SuppressWarnings("static-method") @Test public void test0() {
    assertNull(getAll.invocations(null));
  }
}
