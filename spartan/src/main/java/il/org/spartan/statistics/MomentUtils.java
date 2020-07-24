package il.org.spartan.statistics;

import org.junit.Assert;
import org.junit.Test;

/** @author Yossi Gil
 * @since 31 ביול 2011 */
public class MomentUtils {
  public static double getJarqueBera(final double[] vs) {
    return vs.length * (sqr(skewness(vs)) + sqr(kurotsis(vs) / 2)) / 6;
  }

  public static double getJarqueBera(final RealStatistics ¢) {
    return getJarqueBera(¢.all());
  }

  public static double[] getValues(final RealStatistics ¢) {
    return ¢.values;
  }

  public static double kurotsis(final double... vs) {
    normalize(vs);
    return moment(vs, 4) / pow(moment(vs, 2), 2) - 3;
  }

  public static double kurotsis(final RealStatistics ¢) {
    return kurotsis(¢.all());
  }

  public static double mean(final double... vs) {
    return moment(vs, 1);
  }

  public static double moment(final double[] ds, final int i) {
    return sum(ds, i) / ds.length;
  }

  public static double[] normalize(final double[] $) {
    final double mean = moment($, 1);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] -= mean;
    return $;
  }

  public static double pow(final double d, final int i) {
    return i < 0 ? 1 / pow(d, -i) : i == 0 ? 1 : i == 1 ? d : pow(d, i % 2) * pow(d * d, i / 2);
  }

  public static double skewness(final RealStatistics ¢) {
    return skewness(¢.all());
  }

  public static double sqr(final double ¢) {
    return ¢ * ¢;
  }

  public static double sum(final double[] ds, final int i) {
    double $ = 0;
    for (final double ¢ : ds)
      $ += pow(¢, i);
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

  static double sdCorrection(final int ¢) {
    return Math.sqrt(1. * ¢ / (¢ - 1));
  }

  static double skewenessCorrection(final double... vs) {
    return skewenessCorrection(vs.length);
  }

  static double skewenessCorrection(final int ¢) {
    return Math.sqrt(¢ * (¢ - 1)) / (¢ - 2);
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
      Assert.assertEquals(257201.8765, 4 * pow(40.0625, 3), 1E-3);
      Assert.assertEquals(52140, pow(-44, 3) + pow(-29, 3) + pow(-9, 3) + pow(31, 3) + pow(51, 3), 0);
      Assert.assertEquals(0.2027, 52140 / 257201.8765, 1E-4);
      Assert.assertEquals(0.2027 / skewenessCorrection(vs), skewness(vs), 1E-4);
    }

    // http://www.suite101.com/content/skew-and-how-skewness-is-calculated-in-statistical-software-a231005
    @Test public void skewness2() {
      final double vs[] = { 180, 182, 169, 175, 178, 189, 174, 174, 171, 168 };
      Assert.assertEquals(0.778 / skewenessCorrection(vs), skewness(vs), 1E-4);
    }

    @Test public void testBalancedSkewness() {
      Assert.assertEquals(0.0, skewness(1, 2, 3, 4, 5), 1E-8);
      Assert.assertEquals(0.0, skewness(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1E-8);
    }

    // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
    @Test public void testCorrectedSd() {
      Assert.assertEquals(40.0625, correctedSd(new double[] { 5, 20, 40, 80, 100 }), 1E-4);
    }

    @Test public void testCorrectionValue() {
      Assert.assertEquals(1.490711985, skewenessCorrection(new double[] { 5, 20, 40, 80, 100 }), 1E-4);
    }

    @Test public void testMoment() {
      final double vs[] = { 1, 2, 3, 4, 5 };
      Assert.assertEquals(1, moment(vs, 0), 1E-8);
      Assert.assertEquals(15, sum(vs, 1), 1E-8);
      normalize(vs);
      Assert.assertEquals(1, moment(vs, 0), 1E-8);
      Assert.assertEquals(0, sum(vs, 1), 1E-8);
      Assert.assertEquals(0, moment(vs, 1), 1E-8);
      Assert.assertEquals(2.0, moment(vs, 2), 1E-8);
      Assert.assertEquals(0.0, moment(vs, 3), 1E-8);
      Assert.assertEquals(6.8, moment(vs, 4), 1E-8);
      Assert.assertEquals(0.0, skewness(vs), 1E-8);
    }

    @Test public void testPowers() {
      Assert.assertEquals(1, pow(2, 0), 1E-10);
      Assert.assertEquals(2, pow(2, 1), 1E-10);
      Assert.assertEquals(4, pow(2, 2), 1E-10);
      Assert.assertEquals(8, pow(2, 3), 1E-10);
      Assert.assertEquals(16, pow(2, 4), 1E-10);
      Assert.assertEquals(0.03125, pow(2, -5), 1E-10);
    }

    // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
    @Test public void testSd() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      Assert.assertEquals(Math.sqrt(1605) / sdCorrection(vs), sd(vs), 1E-8);
    }

    @Test public void testSkewenessCorrection() {
      Assert.assertEquals(skewenessCorrection(5), skewenessCorrection(new double[] { 5, 20, 40, 80, 100 }), 1E-10);
    }

    @Test public void testSums() {
      final double vs[] = { 5, 20, 40, 80, 100 };
      Assert.assertEquals(5, sum(vs, 0), 1E-8);
      Assert.assertEquals(245, sum(vs, 1), 1E-8);
      Assert.assertEquals(49, mean(vs), 1E-8);
    }
  }
}
