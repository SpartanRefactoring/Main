/**
 *
 */
package il.org.spartan.utils;

import static il.org.spatan.iteration.Iterables.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Apr 27, 2012 */
public class Denser {
  private static void checkSize(final double[] is, final int n) {
    checkSize(is.length, n);
  }

  private static void checkSize(final int length, final int n) {
    if (length != n)
      throw new IllegalArgumentException("Array of size " + length + " instead of " + n);
  }

  private static void checkSize(final int[] is, final int n) {
    checkSize(is.length, n);
  }

  private static int zeroes(final int[] is) {
    int $ = 0;
    for (final int i : is)
      $ += As.binary(i == 0);
    return $;
  }

  private int n;
  private final int[] gather;

  public Denser(final int... is) {
    gather = new int[(n = is.length) - zeroes(is)];
    int j = 0;
    for (int i = 0; i < is.length; i++)
      if (is[i] != 0)
        gather[j++] = i;
  }

  public double[] gather(final double[] ds) {
    checkSize(ds, n());
    final double[] $ = new double[m()];
    for (int i = 0; i < gather.length; i++)
      $[i] = ds[gather[i]];
    return $;
  }

  public int[] gather(final int... is) {
    checkSize(is, n());
    final int[] $ = new int[m()];
    for (int i = 0; i < gather.length; i++)
      $[i] = is[gather[i]];
    return $;
  }

  public int[][] gather(final int[][] iss) {
    final int[][] $ = new int[iss.length][];
    for (int i = 0; i < iss.length; i++)
      $[i] = gather(iss[i]);
    return $;
  }

  public final int m() {
    return gather.length;
  }

  public final int n() {
    return n;
  }

  public double[] scatter(final double[] ds) {
    checkSize(ds, m());
    final double[] $ = new double[n()];
    for (int i = 0; i < m(); i++)
      $[gather[i]] = ds[i];
    return $;
  }

  public int[] scatter(final int... is) {
    checkSize(is, m());
    final int[] $ = new int[n()];
    for (int i = 0; i < m(); i++)
      $[gather[i]] = is[i];
    return $;
  }

  public XYSeries scatter(final XYSeries s) {
    return new XYSeries(scatter(s.x), scatter(s.y), scatter(s.dy));
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    @Test public void constructorExists() {
      assert null != new Denser(12, 13);
      assert null != new Denser(0, 12, 13);
    }

    @Test public void gatherContent() {
      final int[] g = new Denser(14, 0, 12, 13).gather(11, 1, 2, 4);
      assertEquals(11, g[0]);
      assertEquals(2, g[1]);
      assertEquals(4, g[2]);
    }

    @Test public void gatherDoubles() {
      final double[] g = new Denser(14, 0, 12, 13).gather(doubles(11, 1, 2, 4));
      assertEquals(11, g[0], 1E-5);
      assertEquals(2, g[1], 1E-5);
      assertEquals(4, g[2], 1E-5);
    }

    @Test(expected = IllegalArgumentException.class) //
    public void gatherDoublesIllegalSize() {
      new Denser(14, 0, 12, 13).gather(doubles(11, 1, 2));
    }

    @Test(expected = IllegalArgumentException.class) //
    public void gatherIllegalSize() {
      new Denser(14, 0, 12, 13).gather(11, 1, 2);
    }

    @Test public void gatherMatrix() {
      final int[][] g = new Denser(14, 0, 12, 13).gather(array( //
          ints(11, 12, 13, 14), //
          ints(15, 16, 17, 18), //
          ints(18, 19, 20, 21), //
          ints(21, 22, 23, 24), //
          ints(0, 11, 12, 13) //
      ));
      assertArrayEquals(ints(11, 13, 14), g[0]);
      assertArrayEquals(ints(15, 17, 18), g[1]);
      assertArrayEquals(ints(18, 20, 21), g[2]);
      assertArrayEquals(ints(21, 23, 24), g[3]);
      assertArrayEquals(ints(0, 12, 13), g[4]);
    }

    @Test public void gatherMatrixNotNull() {
      final int[][] g = new Denser(14, 0, 12, 13).gather(array( //
          ints(11, 12, 13, 14), //
          ints(15, 16, 17, 18)));
      assert null != g;
      assertEquals(2, g.length);
    }

    @Test public void gatherMatrixSize() {
      final int[][] g = new Denser(14, 0, 12, 13).gather(array( //
          ints(11, 12, 13, 14), //
          ints(15, 16, 17, 18)));
      assertEquals(2, g.length);
    }

    @Test public void gatherSize() {
      assertEquals(3, new Denser(14, 0, 12, 13).gather(11, 1, 2, 4).length);
    }

    @Test public void m() {
      assertEquals(3, new Denser(14, 0, 12, 13).m());
      assertEquals(4, new Denser(3, 5, 0, 12, 13).m());
    }

    @Test public void n() {
      assertEquals(2, new Denser(12, 13).n());
      assertEquals(3, new Denser(0, 12, 13).n());
    }

    @Test public void scatterContent() {
      final int[] s = new Denser(14, 0, 12, 13).scatter(11, 1, 2);
      assertEquals(11, s[0]);
      assertEquals(0, s[1]);
      assertEquals(1, s[2]);
      assertEquals(2, s[3]);
    }

    @Test public void scatterDoubles() {
      final double[] s = new Denser(14, 0, 12, 13).scatter(doubles(11., 1., 2.));
      assertEquals(11, s[0], 1E-5);
      assertEquals(0, s[1], 1E-5);
      assertEquals(1, s[2], 1E-5);
      assertEquals(2, s[3], 1E-5);
    }

    @Test(expected = IllegalArgumentException.class) //
    public void scatterDoublesIllegalSize() {
      new Denser(14, 0, 12, 13).scatter(doubles(11, 1, 5, 1));
    }

    @Test(expected = IllegalArgumentException.class) //
    public void scatterIllegalSize() {
      new Denser(14, 0, 12, 13).scatter(11, 1, 5, 1);
    }

    @Test public void scatterSize() {
      assertEquals(4, new Denser(14, 0, 12, 13).scatter(11, 1, 2).length);
    }
  }
}
