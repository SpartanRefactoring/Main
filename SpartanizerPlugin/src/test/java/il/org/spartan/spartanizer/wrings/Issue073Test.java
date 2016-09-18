package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixEmptyStringAdditionToString}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue073Test {
  @Test public void issue73_01() {
    trimmingOf("x + \"\"").stays();
  }

  @Test public void issue73_02() {
    trimmingOf("\"abc\" + \"\"").gives("\"abc\"").stays();
  }

  @Test public void issue73_03() {
    trimmingOf("\"\"+\"abc\"").gives("\"abc\"").stays();
  }

  @Test public void issue73_04() {
    trimmingOf("\"abc\" + \"\"+\"abc\"").gives("\"abc\" + \"abc\"");
  }

  @Test public void issue73_05() {
    trimmingOf("x + \"\"+\"abc\"").gives("x + \"abc\"").stays();
  }

  @Test public void issue73_06() {
    trimmingOf("((String)x) + \"\"").gives("((String)x)");
  }
}
