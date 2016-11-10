package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Test class for {@link InfixMultiplicationDistributive}
 * @since 2016 */
@SuppressWarnings("static-method") @Ignore("Disabled: there is some bug in distributive rule - not in Toolbox.") public class issue076 {
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
