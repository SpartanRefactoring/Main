package il.org.spartan.utils;

import static fluent.ly.azzert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Feb 26, 2012 */
public class Binner {
  private static int[] reverse(final int[] $) {
    for (int i = 0, j = $.length - 1; i < j; ++i, --j) {
      final int temp = $[i];
      $[i] = $[j];
      $[j] = temp;
    }
    return $;
  }

  private final int[] from;
  private final int[] to;
  public final int n;
  public final int m;
  private final int[] map;

  public Binner(final int binSize, final int... is) {
    map = new int[n = is.length];
    final IntsArray fromStack = new IntsArray(), toStack = new IntsArray();
    main: for (int t = n; t >= 0; --t)
      for (int f = t - 1, s = 0; f >= 0; --f)
        if (f == 0 || (s += is[f]) >= binSize) {
          fromStack.push(f);
          toStack.push(t);
          t = f + 1;
          continue main;
        }
    from = reverse(fromStack.toArray());
    to = reverse(toStack.toArray());
    ___.sure(from.length == to.length);
    m = from.length;
    for (int i = 0; i < from.length; ++i)
      for (int j = from[i]; j < to[i]; ++j)
        map[j] = i;
  }
  public double[] bin(final double[] ds) {
    ___.require(n == ds.length);
    final double[] $ = new double[m];
    for (int i = 0; i < m; ++i)
      for (int j = from[i]; j < to[i]; ++j)
        $[i] += ds[j];
    return $;
  }
  public int bin(final int j) {
    return map[j];
  }
  public int[] bin(final int[] is) {
    ___.require(n == is.length);
    final int[] $ = new int[m];
    for (int i = 0; i < m; ++i)
      for (int j = from[i]; j < to[i]; ++j)
        $[i] += is[j];
    return $;
  }
  public int[][] bin(final int[][] iss) {
    final int[][] $ = new int[iss.length][];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = bin(iss[¢]);
    return $;
  }
  public double unbin(final double ¢) {
    return unbin((int) (¢ + 0.5));
  }
  public double[] unbin(final double[] ds) {
    final double[] $ = new double[ds.length];
    for (int ¢ = 0; ¢ < ds.length; ++¢)
      $[¢] = unbin(ds[¢]);
    return $;
  }
  public double unbin(final int ¢) {
    return (to[¢] + from[¢] - 1) / 2.;
  }
  public XYSeries unbin(final XYSeries ¢) {
    return new XYSeries(unbin(¢.x), ¢.y, ¢.dy);
  }

  @SuppressWarnings({ "static-method"}) //
  public static class TEST {
    @Test public void binLongRandomArray() {
      final Random r = new Random(0);
      final int[] is = new int[1000];
      for (int ¢ = 0; ¢ < is.length; ++¢)
        is[¢] = r.nextInt(5);
      final Binner b = new Binner(4, is);
      checkBinner(b);
      for (int i = 0; i < b.from.length; ++i) {
        int s = 0;
        for (int j = b.from[i]; j < b.to[i]; ++j)
          s += is[j];
        assert i == 0 || s >= 4;
      }
    }
    @Test public void oneLength() {
      final Binner b = new Binner(1, 1);
      azzert.that(b.from.length, is(1));
      azzert.that(b.to.length, is(1));
      azzert.that(b.from[0], is(0));
      azzert.that(b.to[0], is(1));
    }
    @Test public void singleIntervalOfTwo() {
      checkBinner(new Binner(10, 5, 5));
    }
    @Test public void singleIntervalOfTwoFrom() {
      azzert.that(new Binner(10, 5, 5).from[0], is(0));
    }
    @Test public void singleIntervalOfTwoMap() {
      final Binner b = new Binner(10, 5, 5);
      azzert.that(b.bin(0), is(0));
      azzert.that(b.bin(1), is(0));
    }
    @Test public void singleIntervalOfTwoSize() {
      azzert.that(new Binner(10, 5, 5).n, is(2));
    }
    @Test public void singleIntervalOfTwoTo() {
      azzert.that(new Binner(10, 5, 5).to[0], is(2));
    }
    @Test public void singleIntervalOfTwoUnmap() {
      Assert.assertEquals(0.5, new Binner(10, 5, 5).unbin(0), 1E-12);
    }
    @Test public void singleIntervalUndershootSizeTwo() {
      checkBinner(new Binner(10, 3, 5));
    }
    @Test public void singleIntervalUndershootSizeTwoFrom() {
      azzert.that(new Binner(10, 3, 5).from[0], is(0));
    }
    @Test public void singleIntervalUndershootSizeTwoLengthFromLength() {
      azzert.that(new Binner(10, 3, 5).from.length, is(1));
    }
    @Test public void singleIntervalUndershootSizeTwoLengthToLength() {
      azzert.that(new Binner(10, 3, 5).to.length, is(1));
    }
    @Test public void singleIntervalUndershootSizeTwoTo() {
      azzert.that(new Binner(10, 3, 5).to[0], is(2));
    }
    @Test public void trivial() {
      checkBinner(new Binner(1, 1, 1, 1, 1, 1));
    }
    @Test public void twoIntervalsSizedTwo() {
      final Binner b = new Binner(10, 3, 5, 5, 5);
      azzert.that(b.from[0], is(0));
      azzert.that(b.to[0], is(2));
      azzert.that(b.from[1], is(2));
      azzert.that(b.to[1], is(4));
    }
    @Test public void twoIntervalsSizedTwoBin() {
      final int[] a = new Binner(10, 3, 5, 5, 5).bin(new int[] { 3, 9, 7, 13 });
      azzert.that(a.length, is(2));
      azzert.that(a[0], is(12));
      azzert.that(a[1], is(20));
    }
    @Test public void twoIntervalsSizedTwoBinMatrix() {
      final int[][] a = new Binner(10, 3, 5, 5, 5).bin(new int[][] { //
          new int[] { 3, 9, 7, 13 }, //
          new int[] { 4, 2, 5, 4 }, //
          new int[] { 19, 11, 24, 12 } //
      });
      azzert.that(a.length, is(3));
      azzert.that(a[0][0], is(12));
      azzert.that(a[0][1], is(20));
      azzert.that(a[1][0], is(6));
      azzert.that(a[1][1], is(9));
      azzert.that(a[2][0], is(30));
      azzert.that(a[2][1], is(36));
    }
    @Test public void twoIntervalsSizedTwoDoublesBin() {
      final double[] a = new Binner(10, 3, 5, 5, 5).bin(new double[] { 3, 9, 7, 13 });
      azzert.that(a.length, is(2));
      assertEquals(12.0, a[0], 1E-10);
      assertEquals(20.0, a[1], 1E-10);
    }
    @Test public void twoLength() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.from.length, is(2));
      azzert.that(b.to.length, is(2));
    }
    @Test public void twoSingletonIntervalsFirstInterval() {
      final Binner b = new Binner(1, 1, 1);
      azzert.that(b.to[0], is(1));
      azzert.that(b.from[0], is(0));
    }
    @Test public void twoSingletonIntervalsSecondFrom() {
      azzert.that(new Binner(1, 1, 1).from[1], is(1));
    }
    @Test public void twoSingletonIntervalsSecondTo() {
      azzert.that(new Binner(1, 1, 1).to[1], is(2));
    }
    @Test public void veryLongBinner() {
      checkBinner(new Binner(3, 1, 2, 3, 3, 2, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 0));
    }
    @Test public void zeroLength() {
      final Binner b = new Binner(1);
      azzert.that(b.from.length, is(0));
      azzert.that(b.to.length, is(0));
    }
    private void checkBinner(final Binner ¢) {
      startAtZero(¢);
      endsAtEnd(¢, ¢.n);
      sortedFrom(¢);
      sortedTo(¢);
      nonZeroInterval(¢);
      consecutive(¢);
      checkMap(¢);
      checkInverseMap(¢);
    }
    private void checkInverseMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; ++i)
        for (int j = b.from[i]; j < b.to[i]; ++j) {
          final double u = b.unbin(b.bin(j));
          assert u >= b.from[i];
          assert u < b.to[i];
          assertEquals((b.to[i] + b.from[i] - 1) / 2., u, 1E-12);
        }
    }
    private void checkMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; ++i)
        for (int j = b.from[i]; j < b.to[i]; ++j)
          azzert.that(b.bin(j), is(i));
    }
    private void consecutive(final Binner b) {
      for (int ¢ = 1; ¢ < b.to.length; ++¢)
        azzert.that(b.from[¢], is(b.to[¢ - 1]));
    }
    private void endsAtEnd(final Binner b, final int i) {
      azzert.that(b.from.length, is(b.to.length));
      assert i == 0 || b.to.length > 0;
      azzert.that(b.to[b.to.length - 1], is(i));
    }
    private void nonZeroInterval(final Binner b) {
      for (int ¢ = 0; ¢ < b.to.length; ++¢)
        assert b.to[¢] > b.from[¢];
    }
    private void sortedFrom(final Binner b) {
      for (int ¢ = 0; ¢ < b.from.length - 1; ++¢)
        assert b.from[¢] < b.from[¢ + 1];
    }
    private void sortedTo(final Binner b) {
      for (int ¢ = 0; ¢ < b.to.length - 1; ++¢)
        assert b.to[¢] < b.to[¢ + 1];
    }
    private void startAtZero(final Binner ¢) {
      azzert.that(¢.from[0], is(0));
    }
  }
}
