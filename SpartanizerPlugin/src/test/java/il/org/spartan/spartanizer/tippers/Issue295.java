package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue295 {
  /** Correct way of trimming does not change */
  @Test public void a() {
    trimmingOf("A a = new A();for (A b: as)sum+=b;")//
    .gives("for (A b: new A())sum+=b;") //
    .stays();
  }
}
