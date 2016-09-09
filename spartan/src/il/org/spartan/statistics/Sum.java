/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.mean;
import static il.org.spartan.statistics.MomentUtils.*;
import static il.org.spatan.iteration.Iterables.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-01-08 */
@Utility public enum Sum {
  ;
  public static double sum(final double... ds) {
    double $ = 0;
    for (final double d : ds)
      $ += d;
    return $;
  }

  public static double sum(final double[] t, final int i, final int n) {
    double $ = 0;
    for (int j = i; j < i + n; j++)
      $ += t[j];
    return $;
  }

  public static double sum(final int n, final double... ds) {
    double $ = 0;
    for (final double d : ds)
      $ += pow(d, n);
    return $;
  }

  public static double sum2(final double... ds) {
    double $ = 0;
    for (final double d : ds)
      $ += sqr(d);
    return $;
  }

  @SuppressWarnings({ "static-method" }) //
  public static class TEST {
    @Test public void fullSum() {
      assertEquals(21, sum(doubles(0, 1, 2, 3, 4, 5, 6)), 1E-5);
    }

    @Test public void partialSum() {
      assertEquals(9.0, sum(doubles(0, 1, 2, 3, 4, 5, 6), 2, 3), 1E-5);
    }

    @Test public void sum1() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(5, sum(0, vs), 1E-8);
      assertEquals(245, sum(1, vs), 1E-8);
      assertEquals(49, mean(vs), 1E-8);
    }

    @Test public void sumN() {
      final double vs[] = { 1, 2, 3, 4, 5 };
      assertEquals(5, sum(0, vs), 1E-8);
      assertEquals(15, sum(1, vs), 1E-8);
      assertEquals(1 + 4 + 9 + 16 + 25, sum(2, vs), 1E-8);
      assertEquals(1 + 8 + 27 + 64 + 125, sum(3, vs), 1E-8);
      assertEquals(1 + 16 + 81 + 256 + 625, sum(4, vs), 1E-8);
    }
  }
}
