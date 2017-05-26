package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link LocalInitializedStatementTerminatingScope} Remark:
 * those are tests for issue #54 from bitbucket.
 * @author Ori Roth
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0117 {
  @Test public void issue54ForPlainUseInCondition() {
    trimmingOf("int a  = f(); for (int ¢ = 0; a <100;  ++¢) b[¢] *= 3 << a;")//
        .gives("for (int a  = f(), ¢ = 0; a <100;  ++¢) b[¢] *= 3 << a;")//
        .stays();
  }
  @Test public void issue54ForPlainUseInInitializer() {
    trimmingOf("int a  = f(); for (int ¢ = a; ¢ <100; ++¢) b[¢] = 3;")//
        .gives("for (int ¢ = f(); ¢ <100; ++¢) b[¢] = 3;");
  }
}
