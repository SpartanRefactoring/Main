package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.mean;
import static il.org.spartan.statistics.MomentUtils.*;
import static il.org.spatan.iteration.Iterables.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-01-08 */
@Utility public enum Sum {
  ;
  public static double sum(@NotNull final double... ds) {
    double $ = 0;
    for (final double ¢ : ds)
      $ += ¢;
    return $;
  }

  public static double sum(final double[] t, final int i, final int n) {
    double $ = 0;
    for (int j = i; j < i + n; ++j)
      $ += t[j];
    return $;
  }

  public static double sum(final int i, @NotNull final double... ds) {
    double $ = 0;
    for (final double ¢ : ds)
      $ += pow(¢, i);
    return $;
  }

  public static double sum2(@NotNull final double... ds) {
    double $ = 0;
    for (final double ¢ : ds)
      $ += sqr(¢);
    return $;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void fullSum() {
      Assert.assertEquals(21, sum(doubles(0, 1, 2, 3, 4, 5, 6)), 1E-5);
    }

    @Test public void partialSum() {
      Assert.assertEquals(9.0, sum(doubles(0, 1, 2, 3, 4, 5, 6), 2, 3), 1E-5);
    }

    @Test public void sum1() {
      @NotNull final double vs[] = { 5, 20, 40, 80, 100 };
      Assert.assertEquals(5, sum(0, vs), 1E-8);
      Assert.assertEquals(245, sum(1, vs), 1E-8);
      Assert.assertEquals(49, mean(vs), 1E-8);
    }

    @Test public void sumN() {
      @NotNull final double vs[] = { 1, 2, 3, 4, 5 };
      Assert.assertEquals(5, sum(0, vs), 1E-8);
      Assert.assertEquals(15, sum(1, vs), 1E-8);
      Assert.assertEquals(55, sum(2, vs), 1E-8);
      Assert.assertEquals(225, sum(3, vs), 1E-8);
      Assert.assertEquals(979, sum(4, vs), 1E-8);
    }
  }
}
