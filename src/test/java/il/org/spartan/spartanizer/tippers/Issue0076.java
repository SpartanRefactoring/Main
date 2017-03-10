package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** Test class for {@link InfixMultiplicationDistributive}
 * @since 2016 */
@Ignore("Disabled: there is some bug in distributive rule - not in Toolbox.")
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
