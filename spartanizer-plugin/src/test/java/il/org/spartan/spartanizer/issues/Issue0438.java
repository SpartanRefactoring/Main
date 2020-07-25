package il.org.spartan.spartanizer.issues;

import org.junit.Test;

import il.org.spartan.spartanizer.utils.TestOperandTest;

/** Failing (were ignored) tests of {@link TestOperandTest}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0438 {
  @Test public void placeholder() {
    assert true;
  }
}
