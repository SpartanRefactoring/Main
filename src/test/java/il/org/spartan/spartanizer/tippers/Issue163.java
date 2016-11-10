package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Niv Shalmon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public final class Issue163 {
  @Test public void issue163_01() {
    trimmingOf("return \"remove the block: \" + n + \"\";").gives("return \"remove the block: \" + n;").stays();
  }
  @Test public void issue163_02() {
    trimmingOf("x + \"\" + f() + \"\" + g() + \"abc\"").gives("x + \"\" + f() + g() + \"abc\"").stays();
  }
  @Test public void issue163_03() {
    trimmingOf("x + \"\" + \"\"").gives("x+\"\"").stays();
  }
  @Test public void issue163_04() {
    trimmingOf("\"\"+\"\"+x +\"\"").gives("\"\"+\"\"+x").gives("\"\"+x").gives("x+\"\"").stays();
  }
}
