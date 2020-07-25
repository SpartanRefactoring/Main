package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

/** Unit tests for the GitHub issue thus numbered
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0192 {
  @Ignore // TODO Ori Roth
  @Test public void a() {
    trimmingOf("for (A b : c) if (d(b)) { a = true; break; } return a;")//
        .gives("for (A b : c) if (d(b)) { return true; } return false;")//
        .stays();
  }
}
