package il.org.spartan.spartanizer.issues;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-17 */
public class Issue1008 extends MetaFixture {
  AlphabeticallySortedSentence case1 = new AlphabeticallySortedSentence() {
    /** [[SuppressWarningsSpartan]] */
    @Override protected void startingWith() {/***/
    }

    @Override protected void trimmingStopsAt() {/***/
    }
  };
}
