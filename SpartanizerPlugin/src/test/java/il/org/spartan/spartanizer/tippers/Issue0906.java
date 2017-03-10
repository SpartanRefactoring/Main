package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link PrefixPlusRemove} of previously failed tests.
 * Related to {@link Issue075}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0906 {
  @Test public void issue075h() {
    trimmingOf("int i; i = +0;")//
        .gives("int i = +0;");
  }
}
