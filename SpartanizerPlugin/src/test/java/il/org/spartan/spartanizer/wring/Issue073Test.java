package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link InfixEmptyStringAdditionToString}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue073Test {
  @Test public void issue73_01() {
    trimming("x + \"\"").stays();
  }

  @Test public void issue73_02() {
    trimming("\"abc\" + \"\"").to("\"abc\"");
  }

  @Test public void issue73_03() {
    trimming("\"\"+\"abc\"").to("\"abc\"");
  }

  @Test public void issue73_04() {
    trimming("\"abc\" + \"\"+\"abc\"").to("\"abc\" + \"abc\"");
  }

  @Test public void issue73_05() {
    trimming("x + \"\"+\"abc\"").to("x + \"abc\"");
  }

  @Test public void issue73_06() {
    trimming("((String)x) + \"\"").to("(String)x");
  }
}
