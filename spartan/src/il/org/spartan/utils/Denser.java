package il.org.spartan.utils;

import static fluent.ly.azzert.*;
import static il.org.spatan.iteration.Iterables.*;
import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Apr 27, 2012 */
public class Denser {
  private static void checkSize(@NotNull final double[] is, final int i) {
    checkSize(is.length, i);
  }

  private static void checkSize(final int length, final int i) {
    if (length != i)
      throw new IllegalArgumentException("Array of size " + length + " instead of " + i);
  }

  private static void checkSize(@NotNull final int[] is, final int i) {
    checkSize(is.length, i);
  }

  private static int zeroes(@NotNull final int[] is) {
    int $ = 0;
    for (final int ¢ : is)
      $ += as.bit(¢ == 0);
    return $;
  }

  private int n;
  @NotNull private final int[] gather;

  public Denser(@NotNull final int... is) {
    gather = new int[(n = is.length) - zeroes(is)];
    for (int j = 0, ¢ = 0; ¢ < is.length; ++¢)
      if (is[¢] != 0)
        gather[j++] = ¢;
  }

  @NotNull public double[] gather(@NotNull final double[] ds) {
    checkSize(ds, n());
    @NotNull final double[] $ = new double[m()];
    for (int ¢ = 0; ¢ < gather.length; ++¢)
      $[¢] = ds[gather[¢]];
    return $;
  }

  @NotNull public int[] gather(@NotNull final int... is) {
    checkSize(is, n());
    @NotNull final int[] $ = new int[m()];
    for (int ¢ = 0; ¢ < gather.length; ++¢)
      $[¢] = is[gather[¢]];
    return $;
  }

  @NotNull public int[][] gather(@NotNull final int[][] iss) {
    @NotNull final int[][] $ = new int[iss.length][];
    for (int ¢ = 0; ¢ < iss.length; ++¢)
      $[¢] = gather(iss[¢]);
    return $;
  }

  public final int m() {
    return gather.length;
  }

  public final int n() {
    return n;
  }

  @NotNull public double[] scatter(@NotNull final double[] ds) {
    checkSize(ds, m());
    @NotNull final double[] $ = new double[n()];
    for (int ¢ = 0; ¢ < m(); ++¢)
      $[gather[¢]] = ds[¢];
    return $;
  }

  @NotNull public int[] scatter(@NotNull final int... is) {
    checkSize(is, m());
    @NotNull final int[] $ = new int[n()];
    for (int ¢ = 0; ¢ < m(); ++¢)
      $[gather[¢]] = is[¢];
    return $;
  }

  @NotNull public XYSeries scatter(@NotNull final XYSeries ¢) {
    return new XYSeries(scatter(¢.x), scatter(¢.y), scatter(¢.dy));
  }

  @SuppressWarnings("static-method") //
  public static class TEST {
    @Test public void constructorExists() {
      assert new Denser(12, 13) != null;
      assert new Denser(0, 12, 13) != null;
    }

    @Test public void gatherContent() {
      @NotNull final int[] g = new Denser(14, 0, 12, 13).gather(11, 1, 2, 4);
      azzert.that(g[0], is(11));
      azzert.that(g[1], is(2));
      azzert.that(g[2], is(4));
    }

    @Test public void gatherDoubles() {
      @NotNull final double[] g = new Denser(14, 0, 12, 13).gather(doubles(11, 1, 2, 4));
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
      @NotNull final int[][] g = new Denser(14, 0, 12, 13).gather(array( //
          ints(11, 12, 13, 14), //
          ints(15, 16, 17, 18), //
          ints(18, 19, 20, 21), //
          ints(21, 22, 23, 24), //
          ints(0, 11, 12, 13) //
      ));
      Assert.assertArrayEquals(ints(11, 13, 14), g[0]);
      Assert.assertArrayEquals(ints(15, 17, 18), g[1]);
      Assert.assertArrayEquals(ints(18, 20, 21), g[2]);
      Assert.assertArrayEquals(ints(21, 23, 24), g[3]);
      Assert.assertArrayEquals(ints(0, 12, 13), g[4]);
    }

    @Test public void gatherMatrixNotNull() {
      @NotNull final int[][] g = new Denser(14, 0, 12, 13).gather(array( //
          ints(11, 12, 13, 14), //
          ints(15, 16, 17, 18)));
      assert g != null;
      azzert.that(g.length, is(2));
    }

    @Test public void gatherMatrixSize() {
      azzert.that(new Denser(14, 0, 12, 13).gather(array(ints(11, 12, 13, 14), ints(15, 16, 17, 18))).length, is(2));
    }

    @Test public void gatherSize() {
      azzert.that(new Denser(14, 0, 12, 13).gather(11, 1, 2, 4).length, is(3));
    }

    @Test public void m() {
      azzert.that(new Denser(14, 0, 12, 13).m(), is(3));
      azzert.that(new Denser(3, 5, 0, 12, 13).m(), is(4));
    }

    @Test public void n() {
      azzert.that(new Denser(12, 13).n(), is(2));
      azzert.that(new Denser(0, 12, 13).n(), is(3));
    }

    @Test public void scatterContent() {
      @NotNull final int[] s = new Denser(14, 0, 12, 13).scatter(11, 1, 2);
      azzert.that(s[0], is(11));
      azzert.that(s[1], is(0));
      azzert.that(s[2], is(1));
      azzert.that(s[3], is(2));
    }

    @Test public void scatterDoubles() {
      @NotNull final double[] s = new Denser(14, 0, 12, 13).scatter(doubles(11., 1., 2.));
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
      azzert.that(new Denser(14, 0, 12, 13).scatter(11, 1, 2).length, is(4));
    }
  }
}
