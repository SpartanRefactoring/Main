package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixTermsZero}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0172 {
  @Test public void a01() {
    trimminKof("1+3*x+0")//
        .gives("1+3*x");
  }

  @Test public void a02() {
    trimminKof("1+3*x+0+\"\"")//
        .gives("1+3*x+\"\"");
  }

  @Test public void a03() {
    trimminKof("0+x+\"\"")//
        .stays();
  }

  @Test public void a04() {
    trimminKof("2+1*x+0+\"abc\"+\"\"")//
        .gives("2+1*x+\"abc\"")//
        .gives("1*x+2+\"abc\"")//
        .gives("x+2+\"abc\"")//
        .stays();
  }

  @Test public void a05() {
    trimminKof("x+\"\"+\"abc\"+0")//
        .gives("x+\"abc\"+0")//
        .stays();
  }

  @Test public void a06() {
    trimminKof("0 + \"\"")//
        .stays();
  }

  @Test public void a07() {
    trimminKof("\"\" + 0")//
        .gives("0+\"\"")//
        .stays();
  }

  @Test public void a08() {
    trimminKof("\"\" + 0 + 1")//
        .gives("0+ \"\" + 1")//
        .stays();
  }

  @Test public void a09() {
    trimminKof("x+1+0")//
        .gives("x+1")//
        .stays();
  }

  @Test public void a10() {
    trimminKof("0+x+1")//
        .stays();
  }
}
