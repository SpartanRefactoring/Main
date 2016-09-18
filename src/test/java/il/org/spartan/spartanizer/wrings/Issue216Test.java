package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link CastToLong2Multiply1L}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue216Test {
  @Test public void issue216_01() {
    trimmingOf("(long)1.0").stays();
  }

  @Test public void issue216_02() {
    trimmingOf("(long)1f").stays();
  }

  @Test public void issue216_03() {
    trimmingOf("(long)x").stays();
  }

  @Test public void issue216_04() {
    trimmingOf("(long)1").gives("1L*1").gives("1L").stays();
  }

  @Test public void issue216_05() {
    trimmingOf("(long)'a'").gives("1L*'a'").stays();
  }

  @Test public void issue216_06() {
    trimmingOf("(long)new Integer(5)")//
        .gives("1L*new Integer(5)")//
        .gives("1L*Integer.valueOf(5)")//
        .stays();
  }
}
