/**
 *
 */
package il.org.spartan.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.collections.*;
import il.org.spartan.xy.*;

/** @author Yossi Gil
 * @since Feb 26, 2012 */
public class Binner {
  private static int[] reverse(final int[] $) {
    for (int i = 0, j = $.length - 1; i < j; i++, j--) {
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
    main: for (int t = n; t >= 0; t--)
      for (int f = t - 1, s = 0; f >= 0; f--)
        if (f == 0 || (s += is[f]) >= binSize) {
          fromStack.push(f);
          toStack.push(t);
          t = f + 1;
          continue main;
        }
    from = reverse(fromStack.toArray());
    to = reverse(toStack.toArray());
    ____.sure(from.length == to.length);
    m = from.length;
    for (int i = 0; i < from.length; i++)
      for (int j = from[i]; j < to[i]; j++)
        map[j] = i;
  }

  public double[] bin(final double[] ds) {
    ____.require(n == ds.length);
    final double[] $ = new double[m];
    for (int i = 0; i < m; i++)
      for (int j = from[i]; j < to[i]; j++)
        $[i] += ds[j];
    return $;
  }

  public int bin(final int j) {
    return map[j];
  }

  public int[] bin(final int[] is) {
    ____.require(n == is.length);
    final int[] $ = new int[m];
    for (int i = 0; i < m; i++)
      for (int j = from[i]; j < to[i]; j++)
        $[i] += is[j];
    return $;
  }

  public int[][] bin(final int[][] is) {
    final int[][] $ = new int[is.length][];
    for (int i = 0; i < $.length; i++)
      $[i] = bin(is[i]);
    return $;
  }

  public double unbin(final double d) {
    return unbin((int) (d + 0.5));
  }

  public double[] unbin(final double[] ds) {
    final double[] $ = new double[ds.length];
    for (int i = 0; i < ds.length; i++)
      $[i] = unbin(ds[i]);
    return $;
  }

  public double unbin(final int i) {
    return (from[i] + to[i] - 1) / 2.;
  }

  public XYSeries unbin(final XYSeries s) {
    return new XYSeries(unbin(s.x), s.y, s.dy);
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    @Test public void binLongRandomArray() {
      final Random r = new Random(0);
      final int[] is = new int[1000];
      for (int i = 0; i < is.length; i++)
        is[i] = r.nextInt(5);
      final Binner b = new Binner(4, is);
      checkBinner(b);
      for (int i = 0; i < b.from.length; i++) {
        int s = 0;
        for (int j = b.from[i]; j < b.to[i]; j++)
          s += is[j];
        assertTrue(i == 0 || s >= 4);
      }
    }

    @Test public void oneLength() {
      final Binner b = new Binner(1, 1);
      assertEquals(1, b.from.length);
      assertEquals(1, b.to.length);
      assertEquals(0, b.from[0]);
      assertEquals(1, b.to[0]);
    }

    @Test public void singleIntervalOfTwo() {
      final Binner b = new Binner(10, 5, 5);
      checkBinner(b);
    }

    @Test public void singleIntervalOfTwoFrom() {
      final Binner b = new Binner(10, 5, 5);
      assertEquals(0, b.from[0]);
    }

    @Test public void singleIntervalOfTwoMap() {
      final Binner b = new Binner(10, 5, 5);
      assertEquals(0, b.bin(0));
      assertEquals(0, b.bin(1));
    }

    @Test public void singleIntervalOfTwoSize() {
      final Binner b = new Binner(10, 5, 5);
      assertEquals(2, b.n);
    }

    @Test public void singleIntervalOfTwoTo() {
      final Binner b = new Binner(10, 5, 5);
      assertEquals(2, b.to[0]);
    }

    @Test public void singleIntervalOfTwoUnmap() {
      final Binner b = new Binner(10, 5, 5);
      assertEquals(0.5, b.unbin(0), 1E-12);
    }

    @Test public void singleIntervalUndershootSizeTwo() {
      final Binner b = new Binner(10, 3, 5);
      checkBinner(b);
    }

    @Test public void singleIntervalUndershootSizeTwoFrom() {
      final Binner b = new Binner(10, 3, 5);
      assertEquals(0, b.from[0]);
    }

    @Test public void singleIntervalUndershootSizeTwoLengthFromLength() {
      final Binner b = new Binner(10, 3, 5);
      assertEquals(1, b.from.length);
    }

    @Test public void singleIntervalUndershootSizeTwoLengthToLength() {
      final Binner b = new Binner(10, 3, 5);
      assertEquals(1, b.to.length);
    }

    @Test public void singleIntervalUndershootSizeTwoTo() {
      final Binner b = new Binner(10, 3, 5);
      assertEquals(2, b.to[0]);
    }

    @Test public void trivial() {
      final Binner b = new Binner(1, 1, 1, 1, 1, 1);
      checkBinner(b);
    }

    @Test public void twoIntervalsSizedTwo() {
      final Binner b = new Binner(10, 3, 5, 5, 5);
      assertEquals(0, b.from[0]);
      assertEquals(2, b.to[0]);
      assertEquals(2, b.from[1]);
      assertEquals(4, b.to[1]);
    }

    @Test public void twoIntervalsSizedTwoBin() {
      final int[] a = new Binner(10, 3, 5, 5, 5).bin(new int[] { 3, 9, 7, 13 });
      assertEquals(2, a.length);
      assertEquals(12, a[0]);
      assertEquals(20, a[1]);
    }

    @Test public void twoIntervalsSizedTwoBinMatrix() {
      final int[][] a = new Binner(10, 3, 5, 5, 5).bin(new int[][] { //
          new int[] { 3, 9, 7, 13 }, //
          new int[] { 4, 2, 5, 4 }, //
          new int[] { 19, 11, 24, 12 } //
      });
      assertEquals(3, a.length);
      assertEquals(12, a[0][0]);
      assertEquals(20, a[0][1]);
      assertEquals(6, a[1][0]);
      assertEquals(9, a[1][1]);
      assertEquals(30, a[2][0]);
      assertEquals(36, a[2][1]);
    }

    @Test public void twoIntervalsSizedTwoDoublesBin() {
      final double[] a = new Binner(10, 3, 5, 5, 5).bin(new double[] { 3, 9, 7, 13 });
      assertEquals(2, a.length);
      assertEquals(12.0, a[0], 1E-10);
      assertEquals(20.0, a[1], 1E-10);
    }

    @Test public void twoLength() {
      final Binner b = new Binner(1, 1, 1);
      assertEquals(2, b.from.length);
      assertEquals(2, b.to.length);
    }

    @Test public void twoSingletonIntervalsFirstInterval() {
      final Binner b = new Binner(1, 1, 1);
      assertEquals(1, b.to[0]);
      assertEquals(0, b.from[0]);
    }

    @Test public void twoSingletonIntervalsSecondFrom() {
      final Binner b = new Binner(1, 1, 1);
      assertEquals(1, b.from[1]);
    }

    @Test public void twoSingletonIntervalsSecondTo() {
      final Binner b = new Binner(1, 1, 1);
      assertEquals(2, b.to[1]);
    }

    @Test public void veryLongBinner() {
      final Binner b = new Binner(3, 1, 2, 3, 3, 2, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 0);
      checkBinner(b);
    }

    @Test public void zeroLength() {
      final Binner b = new Binner(1);
      assertEquals(0, b.from.length);
      assertEquals(0, b.to.length);
    }

    private void checkBinner(final Binner b) {
      startAtZero(b);
      endsAtEnd(b, b.n);
      sortedFrom(b);
      sortedTo(b);
      nonZeroInterval(b);
      consecutive(b);
      checkMap(b);
      checkInverseMap(b);
    }

    private void checkInverseMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        for (int j = b.from[i]; j < b.to[i]; j++) {
          final double u = b.unbin(b.bin(j));
          assertTrue(u >= b.from[i]);
          assertTrue(u < b.to[i]);
          assertEquals((b.to[i] + b.from[i] - 1) / 2., u, 1E-12);
        }
    }

    private void checkMap(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        for (int j = b.from[i]; j < b.to[i]; j++)
          assertEquals(i, b.bin(j));
    }

    private void consecutive(final Binner b) {
      for (int i = 1; i < b.to.length; i++)
        assertEquals(b.to[i - 1], b.from[i]);
    }

    private void endsAtEnd(final Binner b, final int n) {
      assertEquals(b.to.length, b.from.length);
      assertTrue(n == 0 || b.to.length > 0);
      assertEquals(n, b.to[b.to.length - 1]);
    }

    private void nonZeroInterval(final Binner b) {
      for (int i = 0; i < b.to.length; i++)
        assertTrue(b.to[i] > b.from[i]);
    }

    private void sortedFrom(final Binner b) {
      for (int i = 0; i < b.from.length - 1; i++)
        assertTrue(b.from[i] < b.from[i + 1]);
    }

    private void sortedTo(final Binner b) {
      for (int i = 0; i < b.to.length - 1; i++)
        assertTrue(b.to[i] < b.to[i + 1]);
    }

    private void startAtZero(final Binner b) {
      assertEquals(0, b.from[0]);
    }
  }
}
