package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit test for {@link DeclarationInitializerStatementTerminatingScope}
 * Remark: those are tests for issue #54 from bitbucket.
 * @author Ori Roth
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings("static-method") public class Issue117 {
  @Test public void issue54ForPlainUseInCondition() {
    trimmingOf("int a  = f(); for (int ¢ = 0; a <100;  ++¢) b[¢] = 3;").gives("for (int a  = f(), ¢ = 0; a <100;  ++¢) b[¢] = 3;").stays();
  }
  @Test public void issue54ForPlainUseInInitializer() {
    trimmingOf("int a  = f(); for (int ¢ = a; ¢ <100; ++¢) b[¢] = 3;").gives(" for (int ¢ = f(); ¢ <100; ++¢) b[¢] = 3;");
  }
}
