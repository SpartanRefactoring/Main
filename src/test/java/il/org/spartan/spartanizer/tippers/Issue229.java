package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for {@link SafeVarargs} in
 * {@link $BodyDeclarationRedundantModifiers}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue229 {
  @Test public void vanilla() {
    trimmingOf("final class X { @SafeVarargs public final void f(final int... __) {}}").stays();
  }
}
