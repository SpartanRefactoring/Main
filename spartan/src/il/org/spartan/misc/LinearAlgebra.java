package il.org.spartan.misc;

import static fluent.ly.azzert.*;
import static il.org.spatan.iteration.Iterables.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.statistics.*;

/** Offers a number of operations on two dimensional matrices and vectors. A
 * <i>vector</i> is simply an array of <code><b>double</b></code>s. Such an
 * array can be thought of as either <i>row</i>-vector or a
 * <i>column</i>-vector.
 * <p>
 * A <i>matrix</i> is an array of <i>rows</i>, where each row is an array
 * <code><b>double</b></code>s. Matrices must be rectalinear (except for method
 * {@link #adjust(double[][])}): All rows of a given matrix must of equal
 * length.
 * <p>
 * Matrices and vectors should never be <code><b>null</b></code>. A
 * <code><b>null</b></code> value shall generate a {@link NullPointerException}.
 * <p>
 * No support is provided to ill defined cases, such as a matrix with zero rows,
 * a matrix with no columns, or multiplication of vectors of different sizes.
 * Such cases shall generate {@link ArrayIndexOutOfBoundsException}.
 * @author Yossi Gil
 * @since February 19, 2012 */
public enum LinearAlgebra {
  ;
  @NotNull public static double[] abs(@NotNull final double[] x) {
    @NotNull final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = Math.abs(x[¢]);
    return $;
  }

  /** Add a scalar to each component of a vector
   * @param y An arbitrary scalar
   * @param x An arbitrary vector
   * @return A newly created vector computed */
  @NotNull public static double[] add(final double y, @NotNull final double[] x) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y;
    return $;
  }

  /** Compute the sum of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] add(@NotNull final double[] x, @NotNull final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y[¢];
    return $;
  }

  /** Compute the sum of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static int[] add(@NotNull final int[] x, @NotNull final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final int[] $ = new int[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y[¢];
    return $;
  }

  @NotNull public static double[][] adjust(@NotNull final double[][] x) {
    final int rows = x.length, longestColumn = longestColumn(x);
    @NotNull final double[][] $ = new double[rows][];
    for (int ¢ = 0; ¢ < rows; ++¢)
      $[¢] = Arrays.copyOf(x[¢], longestColumn);
    return $;
  }

  @NotNull public static int[][] adjust(@NotNull final int[][] $) {
    final int longestColumn = longestColumn($);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = Arrays.copyOf($[¢], longestColumn);
    return $;
  }

  @NotNull public static short[][] adjust(@NotNull final short[][] x) {
    final int rows = x.length, longestColumn = longestColumn(x);
    @NotNull final short[][] $ = new short[rows][];
    for (int ¢ = 0; ¢ < rows; ++¢)
      $[¢] = Arrays.copyOf(x[¢], longestColumn);
    return $;
  }

  public static int columns(final double[][] x) {
    return x[0].length;
  }

  public static int columns(final int[][] x) {
    return x[0].length;
  }

  public static int columns(final short[][] x) {
    return x[0].length;
  }

  public static short demote(final int ¢) {
    if (¢ > Short.MAX_VALUE)
      throw new ArithmeticException("i is " + ¢);
    return (short) ¢;
  }

  @NotNull public static short[] demote(@NotNull final int[] xs) {
    final int n = xs.length;
    @NotNull final short[] $ = new short[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = demote(xs[¢]);
    return $;
  }

  public static double distance(@NotNull final double[] x, @NotNull final double[] y) {
    return norm(sub(x, y));
  }

  /** Divide a given scalar by a vector
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
  @NotNull public static double[] divide(final double x, @NotNull final double y[]) {
    final int n = y.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x / y[¢];
    return $;
  }

  /** Divide a vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
  @NotNull public static double[] divide(@NotNull final double x[], final double y) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y;
    return $;
  }

  @NotNull public static double[] divide(@NotNull final double x[], @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }

  @NotNull public static double[] divide(@NotNull final double x[], @NotNull final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }

  /** Divide a given scalar by a vector
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
  @NotNull public static double[] divide(final double x, @NotNull final int[] y) {
    final int n = y.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x / y[¢];
    return $;
  }

  /** Divide a vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
  @NotNull public static double[] divide(@NotNull final int[] x, final double y) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y;
    return $;
  }

  @NotNull public static double[] divide(@NotNull final int[] x, @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }

  @NotNull public static double[] divide(@NotNull final int[] x, @NotNull final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = 1. * x[¢] / y[¢];
    return $;
  }

  public static double dot(@NotNull final double x[], @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }

  @NotNull public static double[] dot(@NotNull final double a[][], @NotNull final double v[]) {
    final int n = a.length;
    @NotNull final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }

  @NotNull public static double[] dot(@NotNull final int a[][], @NotNull final double v[]) {
    final int n = a.length;
    @NotNull final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }

  public static double dot(@NotNull final int[] x, @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }

  public static double dot(@NotNull final short x[], @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }

  @NotNull public static double[] dot(@NotNull final short a[][], @NotNull final double v[]) {
    final int n = a.length;
    @NotNull final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }

  @NotNull public static double[] fill(@NotNull final double[] x, final double value) {
    Arrays.fill(x, value);
    return x;
  }

  @NotNull public static double[] inverse(@NotNull final double x[]) {
    return divide(1, x);
  }

  public static boolean isReal(final double x) {
    return !Double.isNaN(x) && !Double.isInfinite(x);
  }

  /** Compute the based 10 logarithm of a vector
   * @param x An arbitrary vector
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] log(@NotNull final double[] x) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.log(x[¢]);
    return $;
  }

  @NotNull public static double[][] make(final int rows, final int columns) {
    return new double[rows][columns];
  }

  @NotNull public static int[][] makeFromIntArrays(@NotNull final List<int[]> ¢) {
    return adjust(¢.toArray(new int[0][]));
  }

  @NotNull public static short[][] makeFromShortArrays(@NotNull final List<short[]> ¢) {
    return adjust(¢.toArray(new short[0][]));
  }

  public static double max(@NotNull final double xs[]) {
    double $ = Double.NEGATIVE_INFINITY;
    for (final double x : xs)
      $ = Math.max($, x);
    return $;
  }

  @NotNull public static double[] max(@NotNull final double x[], final double y) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.max(x[¢], y);
    return $;
  }

  public static double min(@NotNull final double xs[]) {
    double $ = Double.POSITIVE_INFINITY;
    for (final double x : xs)
      $ = Math.min($, x);
    return $;
  }

  @NotNull public static double[] min(@NotNull final double x[], final double y) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.min(x[¢], y);
    return $;
  }

  @NotNull public static int[] min(@NotNull final int[] x, final int[] y) {
    final int n = x.length;
    @NotNull final int[] $ = new int[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.min(x[¢], y[¢]);
    return $;
  }

  /** Compute the point wise minimum of the rows of a given matrix
   * @param x An arbitrary matrix
   * @return A newly created vector representing the minimum of the arguments */
  public static int[] min(@NotNull final int[][] x) {
    int[] $ = x[0].clone();
    for (final int[] element : x)
      $ = min($, element);
    return $;
  }

  public static double norm(@NotNull final double x[]) {
    double $ = 0;
    for (final double xʹ : x)
      $ += xʹ * xʹ;
    return Math.sqrt($);
  }

  @NotNull public static double[] normalize(@NotNull final double $[]) {
    final double norm = norm($);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] /= norm;
    return $;
  }

  /** Compute the point-wise product of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] product(@NotNull final double x[], @NotNull final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] * y[¢];
    return $;
  }

  /** Multiply a given vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return a newly created vector representing the product of the two
   *         arguments. */
  @NotNull public static double[] product(final double x, @NotNull final double y[]) {
    final int n = y.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x * y[¢];
    return $;
  }

  /** Compute the point-wise product of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] product(@NotNull final double x[], @NotNull final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] * y[¢];
    return $;
  }

  @NotNull public static double[] promote(@NotNull final int[] x) {
    @NotNull final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = x[¢];
    return $;
  }

  @NotNull public static double[][] promote(@NotNull final int[][] x) {
    @NotNull final double[][] $ = new double[x.length][];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = promote(x[¢]);
    return $;
  }

  @NotNull public static double[] prune(@NotNull final double x[]) {
    final int n = x.length;
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Double.isInfinite(x[¢]) || Double.isNaN(x[¢]) ? 0 : x[¢];
    return $;
  }

  public static int rows(@NotNull final double[][] x) {
    return x.length;
  }

  public static int rows(@NotNull final int[][] x) {
    return x.length;
  }

  public static int rows(@NotNull final short[][] x) {
    return x.length;
  }

  @NotNull public static double[] sqr(@NotNull final double x[]) {
    @NotNull final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = MomentUtils.sqr(x[¢]);
    return $;
  }

  @NotNull public static double[] sqrt(@NotNull final double x[]) {
    @NotNull final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = Math.sqrt(x[¢]);
    return $;
  }

  /** Compute the difference of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] sub(@NotNull final double[] x, @NotNull final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] - y[¢];
    return $;
  }

  /** Subtract two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the result of the
   *         subtraction */
  @NotNull public static double[] subtract(@NotNull final double[] x, @NotNull final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] - y[¢];
    return $;
  }

  /** Subtract two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the result of the
   *         subtraction */
  @NotNull public static int[] subtract(@NotNull final int[] x, @NotNull final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    @NotNull final int[] $ = new int[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] - y[¢];
    return $;
  }

  /** Compute the sum of values in a given vector
   * @param x An arbitrary vector
   * @return the sum of all entries in this vector */
  public static double sum(final double[] x) {
    return Sum.sum(x);
  }

  /** Compute the sum of the rows of a given matrix
   * @param x An arbitrary matrix
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static double[] sum(@NotNull final double[][] x) {
    @NotNull double[] $ = new double[columns(x)];
    for (@NotNull final double[] element : x)
      $ = add($, element);
    return $;
  }

  /** Compute the sum of values in a given vector
   * @param xs An arbitrary vector
   * @return the sum of all entries in this vector */
  public static int sum(@NotNull final int[] xs) {
    int $ = 0;
    for (final int x : xs)
      $ += x;
    return $;
  }

  /** Compute the sum of the rows of a given matrix
   * @param x An arbitrary matrix
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
  @NotNull public static int[] sum(@NotNull final int[][] x) {
    @NotNull int[] $ = new int[columns(x)];
    for (@NotNull final int[] element : x)
      $ = add($, element);
    return $;
  }

  @NotNull public static double[][] transpose(@NotNull final double[][] x) {
    final int rows = x.length, columns = x[0].length;
    @NotNull final double[][] $ = make(columns, rows);
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }

  @NotNull public static int[][] transpose(@NotNull final int[][] x) {
    final int rows = x.length, columns = x[0].length;
    @NotNull final int[][] $ = new int[columns][rows];
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }

  @NotNull public static short[][] transpose(@NotNull final short[][] x) {
    final int rows = x.length, columns = x[0].length;
    @NotNull final short[][] $ = new short[columns][rows];
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }

  @NotNull public static double[] zeroes(final int ¢) {
    return new double[¢];
  }

  private static int longestColumn(@NotNull final double[][] x) {
    int $ = 0;
    for (@NotNull final double[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }

  private static int longestColumn(@NotNull final int[][] x) {
    int $ = 0;
    for (@NotNull final int[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }

  private static int longestColumn(@NotNull final short[][] x) {
    int $ = 0;
    for (@NotNull final short[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void adjust() {
      final int rows = 100;
      @NotNull final double[][] x = new double[100][];
      for (int ¢ = 0; ¢ < rows; ++¢)
        x[¢] = new double[¢];
      @NotNull final double[][] y = LinearAlgebra.adjust(x);
      assertEquals(LinearAlgebra.rows(x), LinearAlgebra.rows(y));
      for (int ¢ = 0; ¢ < rows; ++¢)
        azzert.that("Inappropriate column length at row " + ¢, y[¢].length, is(rows - 1));
    }

    @Test public void isDefinedFalse() {
      assert !isReal(Double.POSITIVE_INFINITY);
      assert !isReal(Double.NEGATIVE_INFINITY);
      assert !isReal(Math.log(0));
      assert !isReal(Double.NaN);
    }

    @Test public void isDefinedTrue() {
      assert isReal(1);
      assert isReal(0);
    }

    @Test public void make() {
      final int rows = 10, columns = 20;
      @NotNull final double[][] __ = LinearAlgebra.make(rows, columns);
      assertEquals(rows, __.length);
      for (int ¢ = 0; ¢ < rows; ++¢)
        azzert.that("Inappropriate column length at row " + ¢, __[¢].length, is(columns));
    }

    @Test public void sqr() {
      Assert.assertArrayEquals(doubles(1, 0, 1, 4), LinearAlgebra.sqr(doubles(-1, 0, 1, 2)), 1E-10);
    }

    @Test public void sum() {
      Assert.assertEquals(2, LinearAlgebra.sum(doubles(-1, 0, 1, 2)), 1E-10);
    }

    @Test public void transpose() {
      final int rows = 10, columns = 20;
      @NotNull final double[][] __ = LinearAlgebra.transpose(LinearAlgebra.make(rows, columns));
      assertEquals(columns, __.length);
      for (int ¢ = 0; ¢ < rows; ++¢)
        azzert.that("Inappropriate column length at row " + ¢, __[¢].length, is(rows));
    }
  }
}
