package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InfixPlusEmptyString;

/** Unit tests for {@link InfixPlusEmptyString}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0073 {
  @Test public void a$01() {
    trimmingOf("x + \"\"")//
        .stays();
  }
  @Test public void a$02() {
    trimmingOf("\"\" + \"abc\" + \"\"")//
        .gives("\"abc\"");
  }
  @Test public void a$03() {
    trimmingOf("\"\"+\"abc\"")//
        .gives("\"abc\"")//
        .stays();
  }
  @Test public void a$04() {
    trimmingOf("\"abc\" + \"\"+\"abc\"")//
        .gives("\"abc\" + \"abc\"");
  }
  @Test public void a$05() {
    trimmingOf("x + \"\"+\"abc\"")//
        .gives("x + \"abc\"")//
        .stays();
  }
  @Test public void a$06() {
    trimmingOf("\"abc\" + \"\" + x")//
        .gives("\"abc\" + x");
  }
  @Test public void a$07() {
    trimmingOf("\"abc\" + \"\"")//
        .gives("\"abc\"")//
        .stays();
  }
  @Test public void a$08() {
    trimmingOf("\"\" + \"abc\"")//
        .gives("\"abc\"");
  }
  @Test public void a$09() {
    trimmingOf("((String)x) + \"\"")//
        .gives("((String)x)");
  }
  @Test public void a$10() {
    trimmingOf("x + \"\"")//
        .stays();
  }
  @Test public void a$11() {
    trimmingOf("\"abc\" + \"\"")//
        .gives("\"abc\"");
  }
}
