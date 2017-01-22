package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Tomer Dragucki
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0192 {
  @Ignore @Test public void a() {
    trimmingOf("boolean a = false; for (A b : c) if (d(b)) { a = true; break; } return a;")
        .gives("for (A b : c) if (d(b)) { return true; } return false;")//
        .stays();
  }
}
