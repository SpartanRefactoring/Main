package il.org.spartan.spartanizer.tippers;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;

/** Failing (were ignored) tests of {@link TrimmerLogTest}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue0438 {
  @Test public void test01() {
    TrimmerLog.tip(null, null);
  }
}
