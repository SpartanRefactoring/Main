package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0006 {
  /** Correct way of trimming does not change */
  @Test public void issue06A() {
    trimmingOf("x/a*-b/-c*- - - d / d")//
        .gives("-x/a*b/c*d / d")//
        .gives("d*-x/a*b/c/d")//
        .gives("b*d*-x/a/c/d")//
        .gives("-b*d*x/a/c/d")//
        .stays();
  }

  @Test public void issue06C2() {
    trimmingOf("-a * b/ c * d/d")//
        .gives("d*-a*b/c/d")//
        .gives("-d*a*b/c/d")//
        .stays();
  }

  @Test public void issue06C3() {
    trimmingOf("-a * b/ c * d") //
        .gives("d*-a*b/c") //
        .gives("-d*a*b/c") //
        .stays();
  }
}
