package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.*;
import static il.org.spartan.statistics.StandardDeviation.*;
import static il.org.spartan.statistics.Sum.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility enum Skewness {
  ;
  public static double skewenessCorrection(@NotNull final double... vs) {
    return skewenessCorrection(vs.length);
  }

  public static double skewenessCorrection(final int ¢) {
    return Math.sqrt(¢ * (¢ - 1)) / (¢ - 2);
  }

  public static double skewness(@NotNull final double... vs) {
    return skewnessNormalizedVector(normalize(vs.clone()));
  }

  public static double skewness(@NotNull final RealStatistics ¢) {
    return skewness(¢.all());
  }

  public static double skewnessNormalizedVector(final double... vs) {
    return moment(3, vs);
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void moment1() {
      @NotNull final double vs[] = { 1, 2, 3, 4, 5 };
      Assert.assertEquals(1, moment(0, vs), 1E-8);
      Assert.assertEquals(15, sum(1, vs), 1E-8);
      Mean.shift(vs);
      Assert.assertEquals(0, moment(1, vs), 1E-8);
      Assert.assertEquals(2.0, moment(2, vs), 1E-8);
      Assert.assertEquals(0.0, moment(3, vs), 1E-8);
      Assert.assertEquals(6.8, moment(4, vs), 1E-8);
      Assert.assertEquals(0.0, skewness(vs), 1E-8);
    }
  }
}
