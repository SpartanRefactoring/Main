package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.ModifierRedundant;

/** Unit tests for {@link SafeVarargs} in {@link ModifierRedundant}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0229 {
  @Test public void vanilla() {
    trimmingOf("final class X { @SafeVarargs public final void f(final int... __) {}}")//
        .stays();
  }
}
