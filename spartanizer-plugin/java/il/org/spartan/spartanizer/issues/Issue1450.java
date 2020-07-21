package il.org.spartan.spartanizer.issues;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

/** Test case for bug in {@link ReturnDeadAssignment}
 * @author Yuval Simon
 * @since 2017-06-29 */
@SuppressWarnings("static-method")
public class Issue1450 {
  @Test public void t1() {
    trimmingOf("class A { int x; int set(int y) { return x = y; } }").stays();
  }
}
