package il.org.spartan;

import static fluent.ly.azzert.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.statistics.ImmutableStatistics;
import il.org.spartan.statistics.RealStatistics;

@SuppressWarnings("static-method")
public class RealStatisticsTest {
  final ImmutableStatistics s = new RealStatistics().record(11).record(20).record(5);
  final ImmutableStatistics s15 = new RealStatistics().record(5).record(3).record(1).record(2).record(4);
  final ImmutableStatistics s07 = new RealStatistics().record(0).record(5).record(6).record(3).record(1).record(2).record(4).record(7);
  final RealStatistics s_4x0_5x1 = new RealStatistics().record(0).record(0).record(0).record(0)//
      .record(1).record(1).record(1).record(1).record(1);

  @Test public void mediannMovingDown() {
    assertEquals(0, s_4x0_5x1.record(0).record(0).median(), 1E-6);
  }
  // @Test public void length() {
  // final Random r = new Random(0);
  // final RealStatistics __ = new RealStatistics();
  // assertEquals(4, __.record(r.nextDouble(), r.nextDouble(), r.nextDouble(),
  // r.nextDouble()).prefixSumRange().length);
  // }
  // @Test public void prefixSumRangeExists() {
  // new RealStatistics().record(10, 20, 30).prefixSumRange();
  // }
  // @Test public void prefixSumRangeLength() {
  // final Random r = new Random(0);
  // final RealStatistics __ = new RealStatistics();
  // __.record(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
  // assertEquals(__.n(), __.prefixSumRange().length);
  // }
  // @Test public void prefixSumRangeToy() {
  // final RealStatistics __ = new RealStatistics();
  // __.record(1, 2, 3, 4, -1, -2, -3, -4);
  // assertEquals(0, __.mean(), 0);
  // assertEquals(8, __.n());
  // final double d[] = __.prefixSumRange();
  // assertEquals(d[0], 0, 0);
  // assertEquals(d[1], 0, 0);
  // assertEquals(d[2], 0, 0);
  // assertEquals(d[3], 0, 0);
  // assertEquals(d[4], 5, 0);
  // assertEquals(d[4], 6, 0);
  // assertEquals(d[4], 7, 0);
  // assertEquals(d[4], 8, 0);
  // }
  @Test public void simpleLength() {
    azzert.that(s.n(), is(3));
    azzert.that(s15.n(), is(5));
    azzert.that(s07.n(), is(8));
    azzert.that(s_4x0_5x1.n(), is(9));
  }
  @Test public void testMax() {
    assertEquals(20, s.max(), 1E-6);
    assertEquals(5, s15.max(), 1E-6);
    assertEquals(7, s07.max(), 1E-6);
    assertEquals(1, s_4x0_5x1.max(), 1E-6);
  }
  @Test public void testMean() {
    assertEquals(12, s.mean(), 1E-6);
    assertEquals(3, s15.mean(), 1E-6);
    assertEquals(3.5, s07.mean(), 1E-6);
    assertEquals(5 / 9.0, s_4x0_5x1.mean(), 1E-6);
  }
  @Test public void testMedian() {
    assertEquals(11, s.median(), 1E-6);
    assertEquals(3, s15.median(), 1E-6);
    assertEquals(3.5, s07.median(), 1E-6);
    assertEquals(1, s_4x0_5x1.median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(1).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(5).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(5).record(6).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(0).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(0).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(0).median(), 1E-6);
    assertEquals(1, s_4x0_5x1.record(0).median(), 1E-6);
    assertEquals(0.5, s_4x0_5x1.record(0).median(), 1E-6);
    assertEquals(0, s_4x0_5x1.record(0).median(), 1E-6);
  }
  @Test(expected = ArithmeticException.class) public void testMedianEmpty() {
    new RealStatistics().median();
  }
  @Test public void testMedianMiddle() {
    assertEquals(0.5, s_4x0_5x1.record(0).median(), 1E-6);
  }
  @Test public void testMedianNotMovingUp() {
    assertEquals(1, s_4x0_5x1.record(5).record(6).median(), 1E-6);
  }
  @Test public void testMin() {
    assertEquals(5, s.min(), 1E-6);
    assertEquals(1, s15.min(), 1E-6);
    assertEquals(0, s07.min(), 1E-6);
    assertEquals(0, s_4x0_5x1.min(), 1E-6);
  }
  @Test public void testSum() {
    assertEquals(36, s.sum(), 1E-6);
    assertEquals(15, s15.sum(), 1E-6);
    assertEquals(28, s07.sum(), 1E-6);
    assertEquals(5.0, s_4x0_5x1.sum(), 1E-6);
  }
}
