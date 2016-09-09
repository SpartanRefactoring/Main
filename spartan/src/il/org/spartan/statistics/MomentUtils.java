/**
 *
 */
package il.org.spartan.statistics;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 31 ביול 2011 */
public class MomentUtils {
  public static double getJarqueBera(final double[] vs) {
    return vs.length * (sqr(skewness(vs)) + sqr(kurotsis(vs) / 2)) / 6;
  }

  public static double getJarqueBera(final RealStatistics s) {
    return getJarqueBera(s.all());
  }

  public static double[] getValues(final RealStatistics s) {
    return s.values;
  }

  public static double kurotsis(final double... vs) {
    normalize(vs);
    return moment(vs, 4) / pow(moment(vs, 2), 2) - 3;
  }

  public static double kurotsis(final RealStatistics s) {
    return kurotsis(s.all());
  }

  public static double mean(final double... vs) {
    return moment(vs, 1);
  }

  public static double moment(final double[] ds, final int n) {
    return sum(ds, n) / ds.length;
  }

  public static double[] normalize(final double[] vs) {
    final double mean = moment(vs, 1);
    for (int i = 0; i < vs.length; i++)
      vs[i] -= mean;
    return vs;
  }

  public static double pow(final double d, final int n) {
    if (n < 0)
      return 1 / pow(d, -n);
    if (n == 0)
      return 1;
    if (n == 1)
      return d;
    return pow(d * d, n / 2) * pow(d, n % 2);
  }

  public static double skewness(final RealStatistics s) {
    return skewness(s.all());
  }

  public static double sqr(final double d) {
    return d * d;
  }

  public static double sum(final double[] ds, final int n) {
    double $ = 0;
    for (final double d : ds)
      $ += pow(d, n);
    return $;
  }

  static double correctedSd(final double... vs) {
    return sd(vs) * sdCorrection(vs);
  }

  static double sd(final double... vs) {
    normalize(vs);
    return Math.sqrt(moment(vs, 2));
  }

  static double sdCorrection(final double... vs) {
    return sdCorrection(vs.length);
  }

  static double sdCorrection(final int n) {
    return Math.sqrt((double) n / (n - 1));
  }

  static double skewenessCorrection(final double... vs) {
    return skewenessCorrection(vs.length);
  }

  static double skewenessCorrection(final int n) {
    return Math.sqrt(n * (n - 1)) / (n - 2);
  }

  static double skewness(final double... vs) {
    normalize(vs);
    return moment(vs, 3) / Math.pow(moment(vs, 2), 1.5);
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
    @Test public void skewness1() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(257201.8765, (5 - 1) * pow(40.0625, 3), 1E-3);
      assertEquals(52140, pow(5 - 49, 3) + pow(20 - 49, 3) + pow(40 - 49, 3) + pow(80 - 49, 3) + pow(100 - 49, 3), 0);
      assertEquals(0.2027, 52140 / 257201.8765, 1E-4);
      assertEquals(0.2027 / skewenessCorrection(vs), skewness(vs), 1E-4);
    }

    // http://www.suite101.com/content/skew-and-how-skewness-is-calculated-in-statistical-software-a231005
    @Test public void skewness2() {
      final double vs[] = { 180, 182, 169, 175, 178, 189, 174, 174, 171, 168 };
      assertEquals(0.778 / skewenessCorrection(vs), skewness(vs), 1E-4);
    }

    @Test public void testBalancedSkewness() {
      assertEquals(0.0, skewness(1, 2, 3, 4, 5), 1E-8);
      assertEquals(0.0, skewness(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1E-8);
    }

    // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
    @Test public void testCorrectedSd() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(40.0625, correctedSd(vs), 1E-4);
    }

    @Test public void testCorrectionValue() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(1.490711985, skewenessCorrection(vs), 1E-4);
    }

    @Test public void testMoment() {
      final double vs[] = { 1, 2, 3, 4, 5 };
      assertEquals(1, moment(vs, 0), 1E-8);
      assertEquals(15, sum(vs, 1), 1E-8);
      normalize(vs);
      assertEquals(1, moment(vs, 0), 1E-8);
      assertEquals(0, sum(vs, 1), 1E-8);
      assertEquals(0, moment(vs, 1), 1E-8);
      assertEquals((4 + 1 + 0 + 1 + 4) / 5.0, moment(vs, 2), 1E-8);
      assertEquals((-9 - 1 + 0 + 1 + 9) / 5.0, moment(vs, 3), 1E-8);
      assertEquals((16 + 1 + 0 + 1 + 16) / 5.0, moment(vs, 4), 1E-8);
      assertEquals(0.0, skewness(vs), 1E-8);
    }

    @Test public void testPowers() {
      assertEquals(1, pow(2, 0), 1E-10);
      assertEquals(2, pow(2, 1), 1E-10);
      assertEquals(4, pow(2, 2), 1E-10);
      assertEquals(8, pow(2, 3), 1E-10);
      assertEquals(16, pow(2, 4), 1E-10);
      assertEquals(1 / 32.0, pow(2, -5), 1E-10);
    }

    // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
    @Test public void testSd() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(Math.sqrt(1605) / sdCorrection(vs), sd(vs), 1E-8);
    }

    @Test public void testSkewenessCorrection() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(skewenessCorrection(5), skewenessCorrection(vs), 1E-10);
    }

    @Test public void testSums() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      assertEquals(5, sum(vs, 0), 1E-8);
      assertEquals(245, sum(vs, 1), 1E-8);
      assertEquals(49, mean(vs), 1E-8);
    }
  }
}
