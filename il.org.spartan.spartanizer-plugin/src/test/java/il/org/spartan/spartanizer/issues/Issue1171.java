package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit test for GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2017-04-03 */
@SuppressWarnings("static-method")
public class Issue1171 {
  @Test public void inlineArrayInitialization2() {
    trimmingOf("final double[] $={ r.z(), r.g()};return $;")//
        .gives("return new double[]{r.z(),r.g()};") //
    ;
  }
}
