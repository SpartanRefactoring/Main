// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static il.org.spartan.utils.____.*;

import java.math.*;
import java.util.*;

import il.org.spartan.streotypes.*;

/** A collection of utility function to generate permutations.
 * @author Yossi Gil,
 * @since 19/06/2008 */
@Utility public enum Permutation {
  ;
  public final static double GOLD = (Math.sqrt(5) - 1) / 2;

  /** @param n a non-negative integer
   * @return the decreasing permutation of length n, represented as an array. */
  public static int[] decreasing(final int n) {
    nonnegative(n);
    final int[] $ = new int[n];
    for (int i = 0; i < n; ++i)
      $[i] = n - i - 1;
    return $;
  }

  /** Compute the factorial of a small integer
   * @param n a given integer
   * @return the factorial of <code>n</code> */
  public static long factorial(final short n) {
    return n <= 1 ? 1 : n * factorial((short) (n - 1));
  }

  /** @param n a non-negative integer
   * @return the increasing permutation of length n, represented as an array. */
  public static int[] identity(final int n) {
    nonnegative(n);
    final int[] $ = new int[n];
    for (int i = 0; i < n; i++)
      $[i] = i;
    return $;
  }

  public static int[] invert(final int[] a) {
    final int[] $ = new int[a.length];
    for (int i = 0; i < a.length; i++)
      $[a[i]] = i;
    return $;
  }

  /** @param n a non-negative integer
   * @return a random permutation of length n, represented as an array. */
  public static int[] random(final int n) {
    nonnegative(n);
    return shuffle(identity(n));
  }

  public static int[] scramble(final int n) {
    final int[] $ = identity(n);
    for (int i = 0; i < n; i++) {
      final double Gi = power(GOLD, i + 1, n);
      System.out.println("Gi=" + Gi);
      final int p = (int) (Gi * n);
      swap($, i, p);
    }
    return $;
  }

  public static float[] shuffle(final float[] a) {
    final Random r = new Random(System.nanoTime());
    for (int i = 0; i < a.length; i++)
      swap(a, i, r.nextInt(a.length));
    return a;
  }

  public static int[] shuffle(final int[] a) {
    final Random r = new Random(System.nanoTime());
    for (int i = 0; i < a.length; i++)
      swap(a, i, r.nextInt(a.length));
    return a;
  }

  public static <T> void shuffle(final T[] ts) {
    final Random r = new Random(System.nanoTime());
    for (int i = 0; i < ts.length; ++i)
      swap(ts, i, r.nextInt(ts.length));
  }

  /** Swap the contents of two <code><b>float</b></code> array cells
   * @param a the array with two cells to be swapped
   * @param i index of this first array cell
   * @param j index of the second array cell */
  public static void swap(final float[] a, final int i, final int j) {
    nonnegative(i);
    nonnegative(j);
    require(i <= a.length);
    require(j <= a.length);
    require(i < a.length);
    require(j < a.length);
    if (i == j)
      return;
    final float temp = a[i];
    a[i] = a[j];
    a[j] = temp;
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

  private static double normalize(final double d, final int n) {
    return normalizeDown(normalizeUp(d, n), n);
  }

  private static double normalizeDown(final double d, final int n) {
    if (d < 0)
      return normalizeDown(-d, n);
    for (double $ = d;; $ *= 1 - 1.0 / n)
      if ($ < 1.0)
        return $;
  }

  private static double normalizeUp(final double d, final int n) {
    if (d < 0)
      return normalizeUp(-d, n);
    if (d == 0)
      return 1.0 / n;
    if (d > 1.0 / n)
      return d;
    for (double $ = d;; $ += $)
      if ($ >= 1.0)
        return $;
  }

  private static double power(final double b, final int k, final int n) {
    if (k == 0)
      return b;
    final double $ = power(normalize(b * b, n), k >> 1, n);
    return (k & 0x1) == 0 ? $ : normalize($ * b, n);
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
