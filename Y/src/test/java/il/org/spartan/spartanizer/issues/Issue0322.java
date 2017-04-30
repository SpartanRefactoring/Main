package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests of issue thus numbered in GitHub
 * @author Yossi Gil
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue0322 {
  @Test public void forIf¢wringGroupisEnabledContinueladd¢() {
    trimmingOf("for (;;) {if (!a.b().c()) continue; d.e(a);}")//
        .gives("for (;;) {if (a.b().c())d.e(a);}") //
        .gives("for(;;)if(a.b().c())d.e(a);") //
        .stays();
  }
}
