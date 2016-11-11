package il.org.spartan.spartanizer.java;

import static il.org.spartan.spartanizer.engine.into.*;
import static org.junit.Assert.*;

import org.junit.*;

/** see issue #813 for more details
 * @author Ron Gatenio
 * @author Roy Shchory
 * @since 16-11-11 */
@SuppressWarnings("static-method")
public class Issue813 {
  @Test public void sameTest() {
    assertTrue(precedence.same(e("a+b"), e("a-b")));
    assertFalse(precedence.same(e("a+b"), e("a*b")));
  }
}
