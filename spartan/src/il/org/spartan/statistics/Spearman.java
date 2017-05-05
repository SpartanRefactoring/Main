package il.org.spartan.statistics;

import java.util.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since Apr 21, 2012 */
public enum Spearman {
  ;
  /** Computes the Spearman rho rank correlation coefficient of two data arrays.
   * @param x first data array
   * @param y second data array
   * @return Returns Spearman's rank correlation coefficient of the parameters
   * @throws IllegalArgumentException if the arrays lengths do not match, of if
   *         the array length is less than 2 */
  public static double rho(@NotNull final double[] x, @NotNull final double[] y) {
    if (x.length != y.length)
      throw new IllegalArgumentException(x.length + ":" + y.length);
    return Pearson.rho(rank(x), rank(y));
  }
  @NotNull private static double[] rank(@NotNull final double[] x) {
    final double[] y = x.clone();
    Arrays.sort(y);
    @NotNull final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = Arrays.binarySearch(y, x[¢]);
    return $;
  }
}
