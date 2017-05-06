package il.org.spartan.statistics;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Apr 21, 2012 */
public enum Pearson {
  ;
  /** Computes the Pearson rho rank correlation coefficient of two data arrays.
   * @param x first data array
   * @param y second data array
   * @return Returns Spearman's rank correlation coefficient for the two arrays,
   *         or NaN if the data has fewer than 2 pairs
   * @throws IllegalArgumentException if the arrays lengths do not match */
  public static double rho( final double[] x,  final double[] y) {
    if (x.length != y.length)
      throw new IllegalArgumentException(x.length + ":" + y.length);
    if (x.length < 2)
      return Double.NaN;
     final WeightedLeastSquares $ = new WeightedLeastSquares();
    $.feed(x, y);
    return $.r();
  }
}
