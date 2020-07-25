package il.org.spartan.spartanizer.issues;

import il.org.spartan.spartanizer.meta.AlphabeticallySortedSentence;
import il.org.spartan.spartanizer.meta.MetaFixture;

/** Unit tests for {@link
 * @author Yossi Gil
 * @since 2017-01-17 */
public class Issue1008 extends MetaFixture {
  AlphabeticallySortedSentence case1 = new AlphabeticallySortedSentence() {
    @Override @SuppressWarnings("all") protected void startingWith() {/***/
    }
    @Override @SuppressWarnings("all") protected void trimmingStopsAt() {/***/
    }
  };
}
