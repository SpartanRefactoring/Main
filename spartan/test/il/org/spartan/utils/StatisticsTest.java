// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.statistics.*;
import il.org.spartan.streotypes.*;

@SuppressWarnings("static-method") @TestCase public class StatisticsTest {
  @Test public void testAverage() {
    final RealStatistics s = new RealStatistics();
    s.record(1);
    s.record(3);
    s.record(2);
    assertEquals(2.0, s.mean(), 1e-7);
  }

  @Test public void testOneCount() {
    final RealStatistics s = new RealStatistics();
    s.record(1);
    assertEquals(1, s.n());
  }

  @Test public void testStdTwoValues() {
    final RealStatistics s = new RealStatistics();
    s.record(8);
    s.record(12);
    assertEquals(2.0, s.sd(), 1e-7);
  }

  @Test public void testTwoValues() {
    final RealStatistics s = new RealStatistics();
    s.record(1);
    s.record(3);
    assertEquals(2.0, s.n(), 1e-7);
  }

  @Test public void testTwoValuesAverage() {
    final RealStatistics s = new RealStatistics();
    s.record(4);
    s.record(6);
    assertEquals(5.0, s.mean(), 1e-7);
  }

  @Test public void testTwoValuesSecondMoment() {
    final RealStatistics s = new RealStatistics();
    s.record(4);
    s.record(6);
    assertEquals(52.0, s.sum2(), 1e-7);
  }

  @Test public void testZeroCount() {
    final Statistics s = new RealStatistics();
    assertEquals(0, s.n());
  }
}
