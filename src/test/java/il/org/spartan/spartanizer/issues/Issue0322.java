package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Tests of issue thus numbered in GitHub
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
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
