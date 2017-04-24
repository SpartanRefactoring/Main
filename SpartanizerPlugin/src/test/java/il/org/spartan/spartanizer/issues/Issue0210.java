package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for {@link InfixDivisionEvaluate}
 * @author Niv Shalmon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0210 {
  @Test public void issue210_01() {
    trimminKof("8/0")//
        .stays();
  }

  // @forget
  @Test public void issue210_02() {
    trimminKof("int zero = 0; int result = 8 / zero; f(++result);")//
        .gives("int zero =0, result = 8 / zero; f(++result);")//
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
