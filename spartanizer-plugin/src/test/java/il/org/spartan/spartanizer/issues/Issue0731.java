package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Test for "Inlining bug- primitives" Issue #731
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-12 */
@SuppressWarnings("static-method")
public class Issue0731 {
  @Test public void a() {
    trimmingOf("Integer i = 0; i.toString();")//
        .stays();
  }
}
