/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.MomentUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 2011-08-2 */
@SuppressWarnings("static-method") public class StatisticsTest {
  @Test public void testBalancedSkewness() {
    assertEquals(0.0, skewness(1, 2, 3, 4, 5), 1E-8);
    assertEquals(0.0, skewness(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1E-8);
  }

  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testCorrectedSd() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    assertEquals(40.0625, correctedSd(vs), 1E-4);
  }

  @Test public void testCorrectionValue() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    assertEquals(1.490711985, skewenessCorrection(vs), 1E-4);
  }

  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testSd() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    assertEquals(Math.sqrt(1605) / sdCorrection(vs), sd(vs), 1E-8);
  }

  @Test public void testSkewenessCorrection() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    assertEquals(skewenessCorrection(5), skewenessCorrection(vs), 1E-10);
  }

  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testSkewness1() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    assertEquals(257201.8765, (5 - 1) * pow(40.0625, 3), 1E-3);
    assertEquals(52140, pow(5 - 49, 3) + pow(20 - 49, 3) + pow(40 - 49, 3) + pow(80 - 49, 3) + pow(100 - 49, 3), 0);
    assertEquals(0.2027, 52140 / 257201.8765, 1E-4);
    assertEquals(0.2027 / skewenessCorrection(vs), skewness(vs), 1E-4);
  }

  // http://www.suite101.com/content/skew-and-how-skewness-is-calculated-in-statistical-software-a231005
  @Test public void testSkewness2() {
    final double vs[] = { 180, 182, 169, 175, 178, 189, 174, 174, 171, 168 };
    assertEquals(0.778 / skewenessCorrection(vs), skewness(vs), 1E-4);
  }
}
