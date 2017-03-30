package il.org.spartan.spartanizer.research.metatester;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/** @author Oren Afek
 * @since 3/27/2017 */
@RunWith(MetaTester.class)
public class SimpleTest {
  @Test @SuppressWarnings("static-method") public void testF() {
    System.out.println("");
    assertEquals(1, 1);
    assertEquals(1, 2);
    System.out.println("");
    assert true;
  }
}
