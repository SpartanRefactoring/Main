package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link MethodInvocationToStringToEmptyStringAddition}
 * @author Niv Shalmon
 * @since 2016
 * @see Issue209Test */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue224Test {
  @Test public void issue224_01() {
    trimmingOf("a+b.toString()").gives("a+\"\"+b").stays();
  }

  @Test public void issue224_02() {
    trimmingOf("b.toString()").gives("\"\"+b").gives("b+\"\"").stays();
  }

  @Test public void issue224_03() {
    trimmingOf("\"5\"+b.toString()").gives("\"5\"+\"\"+b").gives("\"5\"+b").stays();
  }
}
