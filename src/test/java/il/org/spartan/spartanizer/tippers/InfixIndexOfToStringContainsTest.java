package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class InfixIndexOfToStringContainsTest {
  @SuppressWarnings("static-method") @Test public void testMutation3() {
    trimmingOf("\"str\".indexOf(\"stringy\") >= 0").gives("\"str\".contains(\"stringy\")").stays();
  }
}
