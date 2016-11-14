package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class InfixIndexOfToStringContainsTest {
  @SuppressWarnings("static-method") @Test public void testMutation0() {
    trimmingOf("String str; String stringy; return str.indexOf(stringy) >= 0;").gives("String str; String stringy; return str.contains(stringy);")
        .stays();
  }

  @SuppressWarnings("static-method") @Test public void testMutation1() {
    trimmingOf("\"str\".indexOf(\"stringy\") >= 0").gives("\"str\".contains(\"stringy\")").stays();
  }
}