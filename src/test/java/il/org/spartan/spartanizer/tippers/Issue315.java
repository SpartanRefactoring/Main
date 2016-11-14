package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue315 {
  @Test public void a() {
    trimmingOf("for (int iter = 0; true; ++iter)System.out.print(iter);").gives("for (int iter = 0;; ++iter)System.out.print(iter);").stays();
  }
}
