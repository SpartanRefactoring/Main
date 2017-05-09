package il.org.spartan.statistics;

import static il.org.spartan.statistics.MomentUtils.*;

import static fluent.ly.box.*;

import java.util.*;

import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 30/04/2011 */
public abstract class Statistics {
  protected static final int MOMENTS = 4;
  protected static final String EMPTY_SEQUENCE = "No elements yet in sequene.";

  public static double mad(final double[] ds) {
    final int n = ds.length;
    final double median = median(ds), $[] = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.abs(ds[¢] - median);
    return median($);
  }
  public static double median(final double[] ¢) {
    Arrays.sort(¢);
    return (¢[¢.length / 2] + ¢[(¢.length - 1) / 2]) / 2;
  }
  public static double[] prune(final double[] ds) {
    final List<Double> $ = new ArrayList<>();
    final double median = median(ds), mad = mad(ds);
    for (final double ¢ : ds)
      if (median - 2 * mad <= ¢ && ¢ <= median + 2 * mad)
        $.add(box(¢));
    return Iterables.toArray($);
  }
  public static double sampleMean(final double[] ds) {
    double $ = 0;
    for (final double ¢ : ds)
      $ += ¢;
    return $ / ds.length;
  }
  /** Compute a <a href=
   * "http://en.wikipedia.org/wiki/Variance#Population_variance_and_sample_variance"
   * >sample variance</a>
   * @param ds the sample
   * @return the sample variance of the parameter */
  public static double sampleVariance(final double[] ds) {
    double $ = 0, sum2 = 0;
    for (final double ¢ : ds) {
      $ += ¢;
      sum2 += ¢ * ¢;
    }
    return sum2 / (ds.length - 1) - $ * $ / (ds.length * ds.length - ds.length);
  }

  protected int n;
  protected int missing;
  protected double min;
  protected double max;
  protected final double[] moments = new double[MOMENTS];

  /** @return <code><b>true</b></code> <i>if, and only if</i> one ore more
   *         non-missing values were recorded in the sequence. */
  public final boolean isEmpty() {
    return n() == 0;
  }
  /** @return the largest value of the elements in the sequence
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double max() throws ArithmeticException {
    checkEmpty();
    return max;
  }
  /** @return the average value of the elements in the sequence
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double mean() throws ArithmeticException {
    checkEmpty();
    return sum() / n();
  }
  /** @return the smallest value of the elements in the sequence
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double min() throws ArithmeticException {
    checkEmpty();
    return min;
  }
  /** @return a non-negative integer, giving the number of ''missing'' elements
   *         in the sequence */
  public final int missing() {
    return missing;
  }
  /** Provides the number of elements in this instance.
   * @return a non-negative integer, giving the number of elements in the
   *         sequence */
  public final int n() {
    return n;
  }
  /** @return the relative error of the elements in the sequence, defined as the
   *         standard deviation divided by the mean.
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double relativeError() {
    return mean() == 0 ? sd() : sd() / Math.abs(mean());
  }
  /** @return the standard deviation of the elements in the sequence
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double sd() {
    return Math.sqrt(sum2() / n() - sqr(mean()));
  }
  /** @return the sum of all values in the sequence */
  public final double sum() {
    return moments[1];
  }
  /** @return the sum of squares of all values in the sequence */
  public double sum2() {
    return moments[2];
  }
  /** @return the <a href=
   *         "http://en.wikipedia.org/wiki/Variance#Population_variance_and_sample_variance"
   *         >sample variance</a> f the elements in the sequence
   * @throws ArithmeticException in case this function was called prior to
   *         recording any elements in the sequence */
  public final double v() {
    return sum2() / (n - 1) - sqr(sum()) / (sqr(n) - n);
  }
  public double variance() {
    return sum2() / n() - sqr(mean());
  }
  /** Provides the degrees of freedom offered by this instance.
   * @return a non-negative integer, giving the degree of freedom in the
   *         sequence, i.e., the number of elements - 1.
   * @see #n() */
  public final int ν() {
    return n() - 1;
  }
  protected void checkEmpty() throws ArithmeticException {
    if (isEmpty())
      throw new ArithmeticException(EMPTY_SEQUENCE);
  }
}
