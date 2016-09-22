package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixEmptyStringAdditionToString}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue073{
  @Test public void a$01() {
    trimmingOf("x + \"\"").stays();
  }

  @Test public void a$02() {
    trimmingOf("\"abc\" + \"\"").gives("\"abc\"").stays();
  }

  @Test public void a$03() {
    trimmingOf("\"\"+\"abc\"").gives("\"abc\"").stays();
  }

  @Test public void a$04() {
    trimmingOf("\"abc\" + \"\"+\"abc\"").gives("\"abc\" + \"abc\"");
  }

  @Test public void a$05() {
    trimmingOf("x + \"\"+\"abc\"").gives("x + \"abc\"").stays();
  }

  @Test public void a$06() {
    trimmingOf("((String)x) + \"\"").gives("((String)x)");
  }
}
