package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixDivisionEvaluate}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue210 {
  @Test public void issue210_01() {
    trimmingOf("8/0").stays();
  }

  @Test public void issue210_02() {
    trimmingOf("int zero = 0;\nint result = 8 / zero;")//
        .gives("int result = 8 / 0;")//
        .stays();
  }

  @Test public void issue210_03() {
    trimmingOf("8/4.0/0/12").stays();
  }

  @Test public void issue210_04() {
    trimmingOf("x+8l/0").stays();
  }

  @Test public void issue210_05() {
    trimmingOf("8%0").stays();
  }

  @Test public void issue210_06() {
    trimmingOf("8%0l").stays();
  }
}
