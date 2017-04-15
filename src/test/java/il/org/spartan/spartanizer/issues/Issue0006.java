package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0006 {
  /** Correct way of trimming does not change */
  @Test public void issue06A() {
    trimminKof("x/a*-b/-c*- - - d / d")//
        .gives("-x/a*b/c*d / d")//
        .gives("d*-x/a*b/c/d")//
        .gives("b*d*-x/a/c/d")//
        .gives("-b*d*x/a/c/d")//
        .stays();
  }

  @Test public void issue06C2() {
    trimminKof("-a * b/ c * d/d")//
        .gives("d*-a*b/c/d")//
        .gives("-d*a*b/c/d")//
        .stays();
  }

  @Test public void issue06C3() {
    trimminKof("-a * b/ c * d") //
        .gives("d*-a*b/c") //
        .gives("-d*a*b/c") //
        .stays();
  }
}
