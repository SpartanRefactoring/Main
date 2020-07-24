package il.org.spartan.statistics;

import static il.org.spartan.statistics.MomentUtils.correctedSd;
import static il.org.spartan.statistics.MomentUtils.pow;
import static il.org.spartan.statistics.MomentUtils.sd;
import static il.org.spartan.statistics.MomentUtils.sdCorrection;
import static il.org.spartan.statistics.MomentUtils.skewenessCorrection;
import static il.org.spartan.statistics.MomentUtils.skewness;

import org.junit.Assert;
import org.junit.Test;

/** @author Yossi Gil
 * @since 2011-08-2 */
@SuppressWarnings("static-method")
public class StatisticsTest {
  @Test public void testBalancedSkewness() {
    Assert.assertEquals(0.0, skewness(1, 2, 3, 4, 5), 1E-8);
    Assert.assertEquals(0.0, skewness(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1E-8);
  }
  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testCorrectedSd() {
    Assert.assertEquals(40.0625, correctedSd(new double[] { 5, 20, 40, 80, 100 }), 1E-4);
  }
  @Test public void testCorrectionValue() {
    Assert.assertEquals(1.490711985, skewenessCorrection(new double[] { 5, 20, 40, 80, 100 }), 1E-4);
  }
  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testSd() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    Assert.assertEquals(Math.sqrt(1605) / sdCorrection(vs), sd(vs), 1E-8);
  }
  @Test public void testSkewenessCorrection() {
    Assert.assertEquals(skewenessCorrection(5), skewenessCorrection(new double[] { 5, 20, 40, 80, 100 }), 1E-10);
  }
  // http://ncalculators.com/math-worksheets/how-to-find-skewness.htm
  @Test public void testSkewness1() {
    final double vs[] = { 5, 20, 40, 80, 100 };
    Assert.assertEquals(257201.8765, 4 * pow(40.0625, 3), 1E-3);
    Assert.assertEquals(52140, pow(-44, 3) + pow(-29, 3) + pow(-9, 3) + pow(31, 3) + pow(51, 3), 0);
    Assert.assertEquals(0.2027, 52140 / 257201.8765, 1E-4);
    Assert.assertEquals(0.2027 / skewenessCorrection(vs), skewness(vs), 1E-4);
  }
  // http://www.suite101.com/content/skew-and-how-skewness-is-calculated-in-statistical-software-a231005
  @Test public void testSkewness2() {
    final double vs[] = { 180, 182, 169, 175, 178, 189, 174, 174, 171, 168 };
    Assert.assertEquals(0.778 / skewenessCorrection(vs), skewness(vs), 1E-4);
  }
}
