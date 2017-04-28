package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixPlusRemoveParenthesis}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0162 {
  @Test public void issue162_02() {
    trimmingOf("\"I ate\"+(\" an\"+\" ice cream sandwich\")")//
        .gives("\"I ate\"+\" an\"+\" ice cream sandwich\"")//
        .gives("\"I ate an ice cream sandwich\"")//
        .stays();
  }

  @Test public void issue162_03() {
    trimmingOf("(2*3)+\"\"")//
        .gives("2*3+\"\"")//
        .gives("6+\"\"")//
        .stays();
  }

  @Test public void issue162_04() {
    trimmingOf("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_05() {
    trimmingOf("\"a\"+((x-2))")//
        .gives("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_06() {
    trimmingOf("(\"a\")+(x-2)")//
        .gives("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_07() {
    trimmingOf("(x-2)+\"abc\"")//
        .gives("x-2+\"abc\"");
  }

  @Test public void issue162_08() {
    trimmingOf("(f() ? x : y) + \".toString\"")//
        .stays();
  }
}
