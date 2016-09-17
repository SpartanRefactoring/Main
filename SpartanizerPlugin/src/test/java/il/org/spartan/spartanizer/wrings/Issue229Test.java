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
public final class Issue229Test {
  @Test public void vanilla() {
    trimming("final class X { @SafeVarargs public final void f(final int... ¢) {}}").stays();
  }

}
