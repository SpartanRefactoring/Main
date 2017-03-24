package il.org.spartan.spartanizer.issues;

import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-17 */
public class Issue1008 extends MetaFixture {
  @NotNull AlphabeticallySortedSentence case1 = new AlphabeticallySortedSentence() {
    /** [[SuppressWarningsSpartan]] */
    @Override @SuppressWarnings({ "All", "unused" }) protected void startingWith() {
      class A {
        int a;

        A() {
          a = 0;
        }
      }
    }

    /** [[SuppressWarningsSpartan]] */
    @Override @SuppressWarnings({ "All", "unused" }) protected void trimmingStopsAt() {
      class A {
        int a;

        A() {}
      }
    }
  };
}
