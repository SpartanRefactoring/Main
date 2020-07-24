package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Ignore;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InfixMultiplicationDistributive;

/** Test class for {@link InfixMultiplicationDistributive}
 * @since 2016 */
@Ignore("Disabled: there is some bug in distributive rule - not in configuration.")
@SuppressWarnings("static-method")
public class Issue0076 {
  @Test public void issue076a() {
    trimmingOf("a*b + a*c")//
        .gives("a*(b+c)");
  }
  @Test public void issue076b() {
    trimmingOf("b*a + c*a")//
        .gives("a*(b+c)");
  }
  @Test public void issue076c() {
    trimmingOf("b*a + c*a + d*a")//
        .gives("a*(b+c+d)");
  }
}
