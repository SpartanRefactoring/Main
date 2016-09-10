/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.*;
import static il.org.spartan.statistics.Sum.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum StandardDeviation {
  ;
  public static double correctedSd(final double... vs) {
    return sd(vs) * sdCorrection(vs);
  }

  public static double destructiveSd(final double[] vs) {
    return Math.sqrt(destructiveVariance(vs.clone()));
  }

  public static double destructiveVariance(final double[] vs) {
    return destructiveMoment(2, vs);
  }

  public static double[] normalize(final double[] vs) {
    return scale(shift(vs));
  }

  /** Compute a <a href=
   * "http://en.wikipedia.org/wiki/Variance#Population__variance__and__sample__variance"
   * >sample variance</a>
   * @param ds the sample
   * @return the sample variance of the parameter */
  public static double sampleVariance(final double... ds) {
    final double sum = sum(ds);
    final double sum2 = sum2(ds);
    final int n = ds.length;
    return sum2 / (n - 1) - sum * sum / (n * n - n);
  }

  public static double[] scale(final double[] vs) {
    final double sd = sd(vs);
    for (int i = 0; i < vs.length; i++)
      vs[i] /= sd;
    return vs;
  }

  public static double sd(final double... vs) {
    return destructiveSd(vs);
  }

  public static double sdCorrection(final double... vs) {
    return sdCorrection(vs.length);
  }

  public static double sdCorrection(final int n) {
    return Math.sqrt((double) n / (n - 1));
  }

  public static double variance(final double... vs) {
    return destructiveVariance(vs.clone());
  }
}
