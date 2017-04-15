package il.org.spartan.spartanizer.issues;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.utils.*;

/** Failing (were ignored) tests of {@link TestOperandTest}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue0438 {
  @Test public void placeholder() {
    assert true;
  }
}
