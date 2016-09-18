package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixTermsZero}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue172Test {
  @Test public void issue172_01() {
    trimmingOf("1+3*x+0").gives("1+3*x");
  }

  @Test public void issue172_02() {
    trimmingOf("1+3*x+0+\"\"").gives("1+3*x+\"\"");
  }

  @Test public void issue172_03() {
    trimmingOf("0+x+\"\"").stays();
  }

  @Test public void issue172_04() {
    trimmingOf("2+1*x+0+\"abc\"+\"\"").gives("2+1*x+\"abc\"").gives("1*x+2+\"abc\"").gives("x+2+\"abc\"").stays();
  }

  @Test public void issue172_05() {
    trimmingOf("x+\"\"+\"abc\"+0").gives("x+\"abc\"+0").stays();
  }

  @Test public void issue172_06() {
    trimmingOf("0 + \"\"").stays();
  }

  @Test public void issue172_07() {
    trimmingOf("\"\" + 0").gives("0+\"\"").stays();
  }

  @Test public void issue172_08() {
    trimmingOf("\"\" + 0 + 1").gives("0+ \"\" + 1").stays();
  }

  @Test public void issue172_09() {
    trimmingOf("x+1+0").stays();
  }

  @Test public void issue172_10() {
    trimmingOf("0+x+1").stays();
  }
}
