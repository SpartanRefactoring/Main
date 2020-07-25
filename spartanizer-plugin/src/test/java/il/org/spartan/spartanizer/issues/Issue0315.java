package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016-09-23 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0315 {
  @Test public void a() {
    trimmingOf("for (int iter = 0; true; ++iter)System.out.print(iter);")//
        .gives("for (int iter = 0;; ++iter)System.out.print(iter);")//
        .stays();
  }
}
