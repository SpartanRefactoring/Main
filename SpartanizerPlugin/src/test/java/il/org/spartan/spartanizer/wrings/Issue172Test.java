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
  @Test public void a$01() {
    trimmingOf("1+3*x+0").gives("1+3*x");
  }

  @Test public void a$02() {
    trimmingOf("1+3*x+0+\"\"").gives("1+3*x+\"\"");
  }

  @Test public void a$03() {
    trimmingOf("0+x+\"\"").stays();
  }

  @Test public void a$04() {
    trimmingOf("2+1*x+0+\"abc\"+\"\"").gives("2+1*x+\"abc\"").gives("1*x+2+\"abc\"").gives("x+2+\"abc\"").stays();
  }

  @Test public void a$05() {
    trimmingOf("x+\"\"+\"abc\"+0").gives("x+\"abc\"+0").stays();
  }

  @Test public void a$06() {
    trimmingOf("0 + \"\"").stays();
  }

  @Test public void a$07() {
    trimmingOf("\"\" + 0").gives("0+\"\"").stays();
  }

  @Test public void a$08() {
    trimmingOf("\"\" + 0 + 1").gives("0+ \"\" + 1").stays();
  }

  @Test public void a$09() {
    trimmingOf("x+1+0").stays();
  }

  @Test public void a$10() {
    trimmingOf("0+x+1").stays();
  }
}
