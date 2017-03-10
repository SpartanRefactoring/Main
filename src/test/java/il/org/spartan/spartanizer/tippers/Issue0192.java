package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: Tomer Dragucki please add a description
 * @author Tomer Dragucki
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0192 {
  @Ignore // TODO Tomer Dragucki
  @Test public void a() {
    trimmingOf("boolean a = false; for (A b : c) if (d(b)) { a = true; break; } return a;")
        .gives("for (A b : c) if (d(b)) { return true; } return false;")//
        .stays();
  }
}
