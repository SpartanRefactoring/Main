package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/**
 * see issue #714 for more details
 * @author Dan Abramovich
 * @author Assaf Lustig
 * @author Arthur Sapozhnikov
 * @since 16-11-02
 */

public class Issue714 {
  @SuppressWarnings("static-method") @Test public void test0() {
    @SuppressWarnings("unused") final boolean b = determineIf.isImmutable(null);
  }
}
