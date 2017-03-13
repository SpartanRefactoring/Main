package il.org.spartan.spartanizer.issues;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.utils.*;

/** Failing (were ignored) tests of {@link TrimmerLogTest}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class Issue0438 {
  @Test public void test01() {
    TrimmerLog.tip(null, null);
  }
}
