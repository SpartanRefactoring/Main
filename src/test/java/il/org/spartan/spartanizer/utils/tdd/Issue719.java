package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;


/** see issue #719 for more details
 * @author koralchapnik
 * @author yaelAmitay
 * @since 16-11-04 */
public class Issue719 {
  @SuppressWarnings("static-method") @Test public void test() {
    assert true;
  }
  
  @SuppressWarnings("static-method") @Test public void nullCheck() {
    assertFalse(determineIf.definesManyVariables(null, 0));
  }
}
