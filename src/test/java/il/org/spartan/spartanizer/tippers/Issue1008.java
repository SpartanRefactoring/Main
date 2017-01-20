package il.org.spartan.spartanizer.tippers;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
public class Issue1008 extends MetaFixture {
  MetaTestCase case1 = new MetaTestCase() {
    /** [[SuppressWarningsSpartan]] */
    @Override protected void startingWith() {
      @SuppressWarnings("unused")
      class X {
        int a;

        X() {
          a = 1;
        }
      }
    }

    @Override protected void trimmingStopsAt() {
      @SuppressWarnings("unused")
      class X {
        int a = 1;

        X() {}
      }
    }
  };
}
