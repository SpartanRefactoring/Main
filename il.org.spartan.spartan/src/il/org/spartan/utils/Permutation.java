// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static fluent.ly.___.*;

import java.math.*;
import java.util.*;

import il.org.spartan.streotypes.*;

/** A collection of utility function to generate permutations.
 * @author Yossi Gil,
 * @since 19/06/2008 */
@Utility
public enum Permutation {
  ;
  public static final double GOLD = (Math.sqrt(5) - 1) / 2;

  /** @param i a non-negative integer
   * @return the decreasing permutation of length n, represented as an array. */
  public static int[] decreasing(final int i) {
    nonnegative(i);
    final int[] $ = new int[i];
    for (int ¢ = 0; ¢ < i; ++¢)
      $[¢] = i - ¢ - 1;
    return $;
  }
  /** Compute the factorial of a small integer
   * @param n a given integer
   * @return the factorial of <code>n</code> */
  public static long factorial(final short n) {
    return n <= 1 ? 1 : n * factorial((short) (n - 1));
  }
  /** @param i a non-negative integer
   * @return the increasing permutation of length n, represented as an array. */
  public static int[] identity(final int i) {
    nonnegative(i);
    final int[] $ = new int[i];
    for (int ¢ = 0; ¢ < i; ++¢)
      $[¢] = ¢;
    return $;
  }
  public static int[] invert(final int[] a) {
    final int[] $ = new int[a.length];
    for (int ¢ = 0; ¢ < a.length; ++¢)
      $[a[¢]] = ¢;
    return $;
  }
  /** @param ¢ a non-negative integer
   * @return a random permutation of length n, represented as an array. */
  public static int[] random(final int ¢) {
    nonnegative(¢);
    return shuffle(identity(¢));
  }
  public static int[] scramble(final int n) {
    final int[] $ = identity(n);
    for (int i = 0; i < n; ++i) {
      final double Gi = power(GOLD, i + 1, n);
      System.out.println("Gi=" + Gi);
      swap($, i, (int) (n * Gi));
    }
    return $;
  }
  public static float[] shuffle(final float[] $) {
    final Random r = new Random(System.nanoTime());
    for (int ¢ = 0; ¢ < $.length; ++¢)
      swap($, ¢, r.nextInt($.length));
    return $;
  }
  public static int[] shuffle(final int[] $) {
    final Random r = new Random(System.nanoTime());
    for (int ¢ = 0; ¢ < $.length; ++¢)
      swap($, ¢, r.nextInt($.length));
    return $;
  }
  public static <T> void shuffle(final T[] ts) {
    for (int ¢ = 0; ¢ < ts.length; ++¢)
      swap(ts, ¢, new Random(System.nanoTime()).nextInt(ts.length));
  }
  /** Swap the contents of two <code><b>float</b></code> array cells
   * @param fs the array with two cells to be swapped
   * @param i index of this first array cell
   * @param j index of the second array cell */
  public static void swap(final float[] fs, final int i, final int j) {
    nonnegative(i);
    nonnegative(j);
    require(i <= fs.length);
    require(j <= fs.length);
    require(i < fs.length);
    require(j < fs.length);
    if (i == j)
      return;
    final float temp = fs[i];
    fs[i] = fs[j];
    fs[j] = temp;
  }
  /** Swap the contents of two <code><b>int</b></code> array cells
   * @param a the array with two cells to be swapped
   * @param i index of this first array cell
   * @param j index of the second array cell */
  public static void swap(final int[] a, final int i, final int j) {
    nonnegative(i);
    nonnegative(j);
    require(i <= a.length);
    require(j <= a.length);
    require(i < a.length);
    require(j < a.length);
    if (i == j)
      return;
    final int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
  public static <T> void swap(final T[] ts, final int i, final int j) {
    final T t = ts[i];
    ts[i] = ts[j];
    ts[j] = t;
  }
  private static double normalize(final double d, final int i) {
    return normalizeDown(normalizeUp(d, i), i);
  }
  private static double normalizeDown(final double d, final int i) {
    if (d < 0)
      return normalizeDown(-d, i);
    for (double $ = d;; $ *= 1 - 1.0 / i)
      if ($ < 1.0)
        return $;
  }
  private static double normalizeUp(final double d, final int i) {
    if (d < 0)
      return normalizeUp(-d, i);
    if (d == 0)
      return 1.0 / i;
    if (d > 1.0 / i)
      return d;
    for (double $ = d;; $ += $)
      if ($ >= 1.0)
        return $;
  }
  private static double power(final double d, final int k, final int i) {
    if (k == 0)
      return d;
    final double $ = power(normalize(d * d, i), k >> 1, i);
    return (k & 0x1) == 0 ? $ : normalize($ * d, i);
  }

  public static class BigFloat {
    private BigInteger whole;
    private double fraction;

    public double fraction() {
      return fraction;
    }
    public void multiply(final BigFloat other) {
      whole.multiply(other.whole);
      fraction *= other.fraction;
    }
  }
}
