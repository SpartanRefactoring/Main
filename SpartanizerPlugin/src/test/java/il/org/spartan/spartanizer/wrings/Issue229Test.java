package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.wringing.*;

/** Unit tests for {@link SafeVarargs} in
 * {@link AbstractBodyDeclarationRemoveModifiers}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue229Test {
  @Test public void vanilla() {
    trimmingOf("final class X { @SafeVarargs public final void f(final int... ¢) {}}").stays();
  }
}
