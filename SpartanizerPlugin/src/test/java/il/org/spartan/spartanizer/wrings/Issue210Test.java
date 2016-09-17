package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixDivisionEvaluate}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue210Test {
  @Test public void issue210_01() {
    trimming("8/0").stays();
  }

  @Test public void issue210_02() {
    trimming("int zero = 0;\nint result = 8 / zero;").to(" int result = 8 / 0;").stays();
  }

  @Test public void issue210_03() {
    trimming("8/4.0/0/12").stays();
  }

  @Test public void issue210_04() {
    trimming("x+8l/0").stays();
  }

  @Test public void issue210_05() {
    trimming("8%0").stays();
  }

  @Test public void issue210_06() {
    trimming("8%0l").stays();
  }
}
