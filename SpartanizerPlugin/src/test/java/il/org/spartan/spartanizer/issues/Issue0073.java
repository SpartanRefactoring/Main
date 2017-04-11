package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixPlusEmptyString}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0073 {
  @Test public void a$01() {
    topDownTrimming("x + \"\"")//
        .stays();
  }

  @Test public void a$02() {
    topDownTrimming("\"\" + \"abc\" + \"\"")//
        .gives("\"abc\"");
  }

  @Test public void a$03() {
    topDownTrimming("\"\"+\"abc\"")//
        .gives("\"abc\"")//
        .stays();
  }

  @Test public void a$04() {
    topDownTrimming("\"abc\" + \"\"+\"abc\"")//
        .gives("\"abc\" + \"abc\"");
  }

  @Test public void a$05() {
    topDownTrimming("x + \"\"+\"abc\"")//
        .gives("x + \"abc\"")//
        .stays();
  }

  @Test public void a$06() {
    topDownTrimming("\"abc\" + \"\" + x")//
        .gives("\"abc\" + x");
  }

  @Test public void a$07() {
    topDownTrimming("\"abc\" + \"\"")//
        .gives("\"abc\"")//
        .stays();
  }

  @Test public void a$08() {
    topDownTrimming("\"\" + \"abc\"")//
        .gives("\"abc\"");
  }

  @Test public void a$09() {
    topDownTrimming("((String)x) + \"\"")//
        .gives("((String)x)");
  }

  @Test public void a$10() {
    topDownTrimming("x + \"\"")//
        .stays();
  }

  @Test public void a$11() {
    topDownTrimming("\"abc\" + \"\"")//
        .gives("\"abc\"");
  }
}
