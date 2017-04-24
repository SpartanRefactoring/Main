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
    trimminKof("\"I ate\"+(\" an\"+\" ice cream sandwich\")")//
        .gives("\"I ate\"+\" an\"+\" ice cream sandwich\"")//
        .gives("\"I ate an ice cream sandwich\"")//
        .stays();
  }

  @Test public void issue162_03() {
    trimminKof("(2*3)+\"\"")//
        .gives("2*3+\"\"")//
        .gives("6+\"\"")//
        .stays();
  }

  @Test public void issue162_04() {
    trimminKof("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_05() {
    trimminKof("\"a\"+((x-2))")//
        .gives("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_06() {
    trimminKof("(\"a\")+(x-2)")//
        .gives("\"a\"+(x-2)")//
        .stays();
  }

  @Test public void issue162_07() {
    trimminKof("(x-2)+\"abc\"")//
        .gives("x-2+\"abc\"");
  }

  @Test public void issue162_08() {
    trimminKof("(f() ? x : y) + \".toString\"")//
        .stays();
  }
}
