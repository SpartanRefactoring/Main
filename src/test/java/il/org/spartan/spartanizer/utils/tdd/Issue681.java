package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Shay Segal
 * @author Sefi Albo
 * @author Daniel Shames
 * @since 16-11-7 */
@SuppressWarnings("static-method") public class Issue681 {
  @Test public void simpleTest() {
    assertEquals(find.ancestorMethod(null), null);
  }
}