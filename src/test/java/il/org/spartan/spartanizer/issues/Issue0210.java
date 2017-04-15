package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixDivisionEvaluate}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0210 {
  @Test public void issue210_01() {
    trimminKof("8/0")//
        .stays();
  }

  // @Forget
  @Test public void issue210_02() {
    trimminKof("int Zero = 0; int result = 8 / Zero; f(++result);")//
        .gives("int Zero =0, result = 8 / Zero; f(++result);")//
        .stays();
  }

  @Test public void issue210_03() {
    trimminKof("8/4.0/0/12")//
        .stays();
  }

  @Test public void issue210_04() {
    trimminKof("x+8l/0")//
        .stays();
  }

  @Test public void issue210_05() {
    trimminKof("8%0")//
        .stays();
  }

  @Test public void issue210_06() {
    trimminKof("8%0l")//
        .stays();
  }
}
