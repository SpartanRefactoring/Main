/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.*;
import static il.org.spartan.statistics.StandardDeviation.*;
import static il.org.spartan.statistics.Sum.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility enum Skewness {
  ;
  public static double skewenessCorrection(final double... vs) {
    return skewenessCorrection(vs.length);
  }

  public static double skewenessCorrection(final int n) {
    return Math.sqrt(n * (n - 1)) / (n - 2);
  }

  public static double skewness(final double... vs) {
    return skewnessNormalizedVector(normalize(vs.clone()));
  }

  public static double skewness(final RealStatistics s) {
    return skewness(s.all());
  }

  public static double skewnessNormalizedVector(final double... vs) {
    return moment(3, vs);
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void moment1() {
      final double vs[] = { 1, 2, 3, 4, 5 };
      assertEquals(1, moment(0, vs), 1E-8);
      assertEquals(15, sum(1, vs), 1E-8);
      Mean.shift(vs);
      assertEquals(0, moment(1, vs), 1E-8);
      assertEquals((4 + 1 + 0 + 1 + 4) / 5.0, moment(2, vs), 1E-8);
      assertEquals((-9 - 1 + 0 + 1 + 9) / 5.0, moment(3, vs), 1E-8);
      assertEquals((16 + 1 + 0 + 1 + 16) / 5.0, moment(4, vs), 1E-8);
      assertEquals(0.0, skewness(vs), 1E-8);
    }
  }
}
