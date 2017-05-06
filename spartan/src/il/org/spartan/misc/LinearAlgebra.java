package il.org.spartan.misc;

import static fluent.ly.azzert.*;
import static il.org.spatan.iteration.Iterables.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
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
   public static double[] abs( final double[] x) {
     final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = Math.abs(x[¢]);
    return $;
  }
  /** Add a scalar to each component of a vector
   * @param y An arbitrary scalar
   * @param x An arbitrary vector
   * @return A newly created vector computed */
   public static double[] add(final double y,  final double[] x) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y;
    return $;
  }
  /** Compute the sum of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
   public static double[] add( final double[] x,  final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y[¢];
    return $;
  }
  /** Compute the sum of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
   public static int[] add( final int[] x,  final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final int[] $ = new int[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] + y[¢];
    return $;
  }
   public static double[][] adjust( final double[][] x) {
    final int rows = x.length, longestColumn = longestColumn(x);
     final double[][] $ = new double[rows][];
    for (int ¢ = 0; ¢ < rows; ++¢)
      $[¢] = Arrays.copyOf(x[¢], longestColumn);
    return $;
  }
   public static int[][] adjust( final int[][] $) {
    final int longestColumn = longestColumn($);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = Arrays.copyOf($[¢], longestColumn);
    return $;
  }
   public static short[][] adjust( final short[][] x) {
    final int rows = x.length, longestColumn = longestColumn(x);
     final short[][] $ = new short[rows][];
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
   public static short[] demote( final int[] xs) {
    final int n = xs.length;
     final short[] $ = new short[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = demote(xs[¢]);
    return $;
  }
  public static double distance( final double[] x,  final double[] y) {
    return norm(sub(x, y));
  }
  /** Divide a given scalar by a vector
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
   public static double[] divide(final double x,  final double y[]) {
    final int n = y.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x / y[¢];
    return $;
  }
  /** Divide a vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
   public static double[] divide( final double x[], final double y) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y;
    return $;
  }
   public static double[] divide( final double x[],  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }
   public static double[] divide( final double x[],  final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }
  /** Divide a given scalar by a vector
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
   public static double[] divide(final double x,  final int[] y) {
    final int n = y.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x / y[¢];
    return $;
  }
  /** Divide a vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return A newly created vector representing the multiplication of the
   *         arguments. */
   public static double[] divide( final int[] x, final double y) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y;
    return $;
  }
   public static double[] divide( final int[] x,  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] / y[¢];
    return $;
  }
   public static double[] divide( final int[] x,  final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = 1. * x[¢] / y[¢];
    return $;
  }
  public static double dot( final double x[],  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }
   public static double[] dot( final double a[][],  final double v[]) {
    final int n = a.length;
     final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }
   public static double[] dot( final int a[][],  final double v[]) {
    final int n = a.length;
     final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }
  public static double dot( final int[] x,  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }
  public static double dot( final short x[],  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
    double $ = 0;
    for (int ¢ = 0; ¢ < n; ++¢)
      $ += x[¢] * y[¢];
    return $;
  }
   public static double[] dot( final short a[][],  final double v[]) {
    final int n = a.length;
     final double[] $ = new double[a.length];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = dot(a[¢], v);
    return $;
  }
   public static double[] fill( final double[] x, final double value) {
    Arrays.fill(x, value);
    return x;
  }
   public static double[] inverse( final double x[]) {
    return divide(1, x);
  }
  public static boolean isReal(final double x) {
    return !Double.isNaN(x) && !Double.isInfinite(x);
  }
  /** Compute the based 10 logarithm of a vector
   * @param x An arbitrary vector
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
   public static double[] log( final double[] x) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.log(x[¢]);
    return $;
  }
   public static double[][] make(final int rows, final int columns) {
    return new double[rows][columns];
  }
   public static int[][] makeFromIntArrays( final List<int[]> ¢) {
    return adjust(¢.toArray(new int[0][]));
  }
   public static short[][] makeFromShortArrays( final List<short[]> ¢) {
    return adjust(¢.toArray(new short[0][]));
  }
  public static double max( final double xs[]) {
    double $ = Double.NEGATIVE_INFINITY;
    for (final double x : xs)
      $ = Math.max($, x);
    return $;
  }
   public static double[] max( final double x[], final double y) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.max(x[¢], y);
    return $;
  }
  public static double min( final double xs[]) {
    double $ = Double.POSITIVE_INFINITY;
    for (final double x : xs)
      $ = Math.min($, x);
    return $;
  }
   public static double[] min( final double x[], final double y) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.min(x[¢], y);
    return $;
  }
   public static int[] min( final int[] x, final int[] y) {
    final int n = x.length;
     final int[] $ = new int[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.min(x[¢], y[¢]);
    return $;
  }
  /** Compute the point wise minimum of the rows of a given matrix
   * @param x An arbitrary matrix
   * @return A newly created vector representing the minimum of the arguments */
  public static int[] min( final int[][] x) {
    int[] $ = x[0].clone();
    for (final int[] element : x)
      $ = min($, element);
    return $;
  }
  public static double norm( final double x[]) {
    double $ = 0;
    for (final double xʹ : x)
      $ += xʹ * xʹ;
    return Math.sqrt($);
  }
   public static double[] normalize( final double $[]) {
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
   public static double[] product( final double x[],  final double y[]) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] * y[¢];
    return $;
  }
  /** Multiply a given vector by a scalar
   * @param x a scalar
   * @param y an arbitrary vector
   * @return a newly created vector representing the product of the two
   *         arguments. */
   public static double[] product(final double x,  final double y[]) {
    final int n = y.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x * y[¢];
    return $;
  }
  /** Compute the point-wise product of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created representing the Hadamard product of the
   *         arguments */
   public static double[] product( final double x[],  final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] * y[¢];
    return $;
  }
   public static double[] promote( final int[] x) {
     final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = x[¢];
    return $;
  }
   public static double[][] promote( final int[][] x) {
     final double[][] $ = new double[x.length][];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = promote(x[¢]);
    return $;
  }
   public static double[] prune( final double x[]) {
    final int n = x.length;
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Double.isInfinite(x[¢]) || Double.isNaN(x[¢]) ? 0 : x[¢];
    return $;
  }
  public static int rows( final double[][] x) {
    return x.length;
  }
  public static int rows( final int[][] x) {
    return x.length;
  }
  public static int rows( final short[][] x) {
    return x.length;
  }
   public static double[] sqr( final double x[]) {
     final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = MomentUtils.sqr(x[¢]);
    return $;
  }
   public static double[] sqrt( final double x[]) {
     final double[] $ = new double[x.length];
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = Math.sqrt(x[¢]);
    return $;
  }
  /** Compute the difference of two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
   public static double[] sub( final double[] x,  final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] - y[¢];
    return $;
  }
  /** Subtract two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the result of the
   *         subtraction */
   public static double[] subtract( final double[] x,  final double[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final double[] $ = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = x[¢] - y[¢];
    return $;
  }
  /** Subtract two given vectors.
   * @param x An arbitrary vector
   * @param y Another arbitrary vector of equal length
   * @return A newly created vector representing the result of the
   *         subtraction */
   public static int[] subtract( final int[] x,  final int[] y) {
    assert x.length == y.length;
    final int n = Math.max(x.length, y.length);
     final int[] $ = new int[n];
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
   public static double[] sum( final double[][] x) {
     double[] $ = new double[columns(x)];
    for ( final double[] element : x)
      $ = add($, element);
    return $;
  }
  /** Compute the sum of values in a given vector
   * @param xs An arbitrary vector
   * @return the sum of all entries in this vector */
  public static int sum( final int[] xs) {
    int $ = 0;
    for (final int x : xs)
      $ += x;
    return $;
  }
  /** Compute the sum of the rows of a given matrix
   * @param x An arbitrary matrix
   * @return A newly created vector representing the Hadamard product of the
   *         arguments */
   public static int[] sum( final int[][] x) {
     int[] $ = new int[columns(x)];
    for ( final int[] element : x)
      $ = add($, element);
    return $;
  }
   public static double[][] transpose( final double[][] x) {
    final int rows = x.length, columns = x[0].length;
     final double[][] $ = make(columns, rows);
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }
   public static int[][] transpose( final int[][] x) {
    final int rows = x.length, columns = x[0].length;
     final int[][] $ = new int[columns][rows];
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }
   public static short[][] transpose( final short[][] x) {
    final int rows = x.length, columns = x[0].length;
     final short[][] $ = new short[columns][rows];
    for (int i = 0; i < rows; ++i)
      for (int j = 0; j < columns; ++j)
        $[j][i] = x[i][j];
    return $;
  }
   public static double[] zeroes(final int ¢) {
    return new double[¢];
  }
  private static int longestColumn( final double[][] x) {
    int $ = 0;
    for ( final double[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }
  private static int longestColumn( final int[][] x) {
    int $ = 0;
    for ( final int[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }
  private static int longestColumn( final short[][] x) {
    int $ = 0;
    for ( final short[] xʹ : x)
      $ = Math.max($, xʹ.length);
    return $;
  }

  @SuppressWarnings("static-method")
  public static class TEST {
    @Test public void adjust() {
      final int rows = 100;
       final double[][] x = new double[100][];
      for (int ¢ = 0; ¢ < rows; ++¢)
        x[¢] = new double[¢];
       final double[][] y = LinearAlgebra.adjust(x);
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
       final double[][] __ = LinearAlgebra.make(rows, columns);
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
       final double[][] __ = LinearAlgebra.transpose(LinearAlgebra.make(rows, columns));
      assertEquals(columns, __.length);
      for (int ¢ = 0; ¢ < rows; ++¢)
        azzert.that("Inappropriate column length at row " + ¢, __[¢].length, is(rows));
    }
  }
}
