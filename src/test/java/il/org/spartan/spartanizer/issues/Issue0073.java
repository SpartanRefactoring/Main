package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixPlusEmptyString}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0073 {
  @Test public void a$01() {
    trimminKof("x + \"\"")//
        .stays();
  }

  @Test public void a$02() {
    trimminKof("\"\" + \"abc\" + \"\"")//
        .gives("\"abc\"");
  }

  @Test public void a$03() {
    trimminKof("\"\"+\"abc\"")//
        .gives("\"abc\"")//
        .stays();
  }

  @Test public void a$04() {
    trimminKof("\"abc\" + \"\"+\"abc\"")//
        .gives("\"abc\" + \"abc\"");
  }

  @Test public void a$05() {
    trimminKof("x + \"\"+\"abc\"")//
        .gives("x + \"abc\"")//
        .stays();
  }

  @Test public void a$06() {
    trimminKof("\"abc\" + \"\" + x")//
        .gives("\"abc\" + x");
  }

  @Test public void a$07() {
    trimminKof("\"abc\" + \"\"")//
        .gives("\"abc\"")//
        .stays();
  }

  @Test public void a$08() {
    trimminKof("\"\" + \"abc\"")//
        .gives("\"abc\"");
  }

  @Test public void a$09() {
    trimminKof("((String)x) + \"\"")//
        .gives("((String)x)");
  }

  @Test public void a$10() {
    trimminKof("x + \"\"")//
        .stays();
  }

  @Test public void a$11() {
    trimminKof("\"abc\" + \"\"")//
        .gives("\"abc\"");
  }
}
