package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.*;
import static il.org.spartan.statistics.Sum.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility
public enum StandardDeviation {
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
   * "http://en.wikipedia.org/wiki/Variance#Population_variance_and_sample_variance"
   * >sample variance</a>
   * @param ¢ the sample
   * @return the sample variance of the parameter */
  public static double sampleVariance(final double... ¢) {
    final double $ = sum(¢);
    return sum2(¢) / (¢.length - 1) - $ * $ / (¢.length * ¢.length - ¢.length);
  }
  public static double[] scale(final double[] $) {
    final double sd = sd($);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] /= sd;
    return $;
  }
  public static double sd(final double... vs) {
    return destructiveSd(vs);
  }
  public static double sdCorrection(final double... vs) {
    return sdCorrection(vs.length);
  }
  public static double sdCorrection(final int ¢) {
    return Math.sqrt(1. * ¢ / (¢ - 1));
  }
  public static double variance(final double... vs) {
    return destructiveVariance(vs.clone());
  }
}
