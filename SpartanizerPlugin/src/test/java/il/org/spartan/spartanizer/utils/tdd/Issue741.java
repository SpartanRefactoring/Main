package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

/** @author Shimon Azulay & Idan Atias & Lior Ben Ami
 * @since 16-11-3 */
@SuppressWarnings({ "static-method", "javadoc" }) public class Issue741 {
  @Test public void publicFields_test0() {
    getAll2.publicFields(null);
    assert true;
  }

  @SuppressWarnings("cast") @Test public void publicFields_test1() {
    assertTrue(getAll2.publicFields(null) instanceof List<?>);
  }

  @Test public void publicFields_test2() {
    assertNotNull(getAll2.publicFields(null));
  }

}
