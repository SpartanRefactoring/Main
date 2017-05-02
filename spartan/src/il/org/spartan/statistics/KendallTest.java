package il.org.spartan.statistics;

import static fluent.ly.azzert.*;
import static il.org.spatan.iteration.Iterables.*;
import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;

@SuppressWarnings("static-method") public class KendallTest {
  @Test public void test10() {
    assertEquals(0.6, Kendall.tau(doubles(15, 20, 100, 70, 98)), 1E-6);
  }

  @Test public void test10tauB() {
    assertEquals(0.6, Kendall.tauB(doubles(15, 20, 100, 70, 98)), 1E-6);
  }

  @Test public void testMinorityVsChi2() {
    @NotNull final double[]//
    x = { 1, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 3, 1, 3, 3, 1, 3, 1, 3, 3, 0, 2, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 2,
        1, 2, 1, 2, 2, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 3, 4, 3, 0, 1, 1, 1, 1, 1 },
        y = { 1, 5, 1, 0, 3, 0, 0, 2, 5, 4, 3, 3, 0, 0, 1, 5, 3, 0, 1, 2, 4, 3, 3, 4, 1, 8, 0, 2, 6, 2, 3, 2, 11, 8, 10, 3, 8, 7, 1, 1, 2, 4, 1, 3, 3,
            1, 2, 5, 0, 4, 0, 1, 1, 3, 6, 3, 2, 2, 4, 1, 3, 2, 3, 1, 8, 2, 8, 1, 7, 6, 2, 5, 2, 8, 7, 6 };
    final double tau = Kendall.tauB(x, y);
    assertEquals(-0.0929, tau, 0.0001);
    assertEquals(3.7983, new Kendall.Charectristics(y.length, tau).z, 0.001);
  }

  @Test public void testMinorityVsTypesChange() {
    @NotNull final double[] x = { 1, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 3, 1, 3, 3, 1, 3, 1, 3, 3, 0, 2, 1, 0, 0, 1, 0,
        0, 1, 0, 0, 1, 1, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 3, 4, 3, 0, 1, 1, 1, 1, 1 },
        y = { 1.696078431, 1.080924855, 1.417112299, 1, 1.513207547, 1.012468828, 1.002463054, 1.285012285, 1.057361377, 1.04159132, 1.305555556,
            1.022606383, 1.019607843, 1.096153846, 1.087719298, 3.112903226, 1.088082902, 1, 1.033333333, 1.032258065, 1.02346707, 1.068047337,
            1.132271468, 1.053275109, 1.272802653, 1.321172638, 1.018244576, 1.104116223, 1.071052632, 1.305896806, 1.007212292, 1.025840598,
            1.508042489, 1.082310324, 1.19821495, 1.120198265, 1.243362832, 1.196619217, 1.395604396, 1.976377953, 1.035856574, 1.131034483,
            1.154471545, 1.117957746, 1.441441441, 1.3, 1.009615385, 1.454761905, 1.009819967, 1.126418152, 1.020143885, 1.005641749, 1.019635344,
            1.017881706, 1.776384535, 5.172413793, 1.723333333, 1.037037037, 1.1, 1.022727273, 1.033333333, 1.429319372, 2.060606061, 1.584558824,
            1.635730858, 2.452380952, 1.825242718, 1.597826087, 1.721088435, 1.071146245, 1.351851852, 1.216894977, 1.034482759, 3.814814815,
            1.993527508, 1.660714286 };
    final double tau = Kendall.tauB(x, y);
    assertEquals(-0.3549719, tau, 0.0001);
    assertEquals(4.537, new Kendall.Charectristics(y.length, tau).z, 0.0001);
  }

  @Test public void testPairs() {
    azzert.that(Kendall.pairs(1), is(0));
    azzert.that(Kendall.pairs(2), is(1));
    azzert.that(Kendall.pairs(3), is(3));
    azzert.that(Kendall.pairs(4), is(6));
  }

  @Test public void testSigmaDistinactAndPair() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 2, 5, 5 }), is(1));
  }

  @Test public void testSigmaDistinactAndQuad() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 2, 5, 5, 5, 5 }), is(6));
  }

  @Test public void testSigmaDistinactAndTriple() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 2, 5, 5, 5 }), is(3));
  }

  @Test public void testSigmaDistinctValues() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 }), is(0));
  }

  @Test public void testSigmaPair() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4 }), is(1));
  }

  @Test public void testSigmaPairAndDistinct() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 5 }), is(1));
  }

  @Test public void testSigmaPairAndPair() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 5, 5 }), is(2));
  }

  @Test public void testSigmaQuad() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4, 4 }), is(6));
  }

  @Test public void testSigmaQuadAndDistinct() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4, 4, 5 }), is(6));
  }

  @Test public void testSigmaQuadAndPair() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4, 4, 5, 5 }), is(7));
  }

  @Test public void testSigmaTriple() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4 }), is(3));
  }

  @Test public void testSigmaTripleAndDistinct() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4, 5 }), is(3));
  }

  @Test public void testSigmaTripleAndPair() {
    azzert.that(Kendall.sigmaSortedArray(new double[] { 4, 4, 4, 5, 5 }), is(4));
  }

  @Test public void testTauBExample2() {
    assertEquals(0.5606061,
        Kendall.tau(new double[] { 68, 70, 71, 71, 72, 77, 77, 86, 87, 88, 91, 91 }, new double[] { 64, 65, 77, 80, 72, 65, 76, 88, 72, 81, 90, 96 }),
        0.0001);
  }

  @Test public void testTauBExample2WithTauB() {
    assertEquals(0.5737, Kendall.tauB(new double[] { 68, 70, 71, 71, 72, 77, 77, 86, 87, 88, 91, 91 },
        new double[] { 64, 65, 77, 80, 72, 65, 76, 88, 72, 81, 90, 96 }), 0.0001);
  }

  @Test public void testTauBExampleWithTau() {
    assertEquals(-0.1105263, Kendall.tau(new double[] { 0, 0, 0, 0, 20, 20, 0, 60, 0, 20, 10, 10, 0, 40, 0, 20, 0, 0, 0, 0 },
        new double[] { 0, 80, 80, 80, 10, 33, 60, 0, 67, 27, 25, 80, 80, 80, 80, 80, 80, 0, 10, 45 }), 0.0001);
  }

  @Test public void testTauBExampleWithTauB() {
    assertEquals(-0.12069, Kendall.tauB(new double[] { 0, 0, 0, 0, 20, 20, 0, 60, 0, 20, 10, 10, 0, 40, 0, 20, 0, 0, 0, 0 },
        new double[] { 0, 80, 80, 80, 10, 33, 60, 0, 67, 27, 25, 80, 80, 80, 80, 80, 80, 0, 10, 45 }), 0.0001);
  }

  @Test public void testTauBIdentical() {
    @NotNull final double[] x = { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 };
    assertEquals(1, Kendall.tauB(x, x), 0.0001);
  }

  @Test public void testTauBNegated() {
    assertEquals(-1, Kendall.tauB(new double[] { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 }, new double[] { -4, -10, -3, -1, -9, -2, -6, -7, -8, -5 }), 0.0001);
  }

  @Test public void testTauIdentical() {
    @NotNull final double[] x = { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 };
    assertEquals(1, Kendall.tau(x, x), 0.0001);
  }

  @Test public void testTauNegated() {
    assertEquals(-1, Kendall.tau(new double[] { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 }, new double[] { -4, -10, -3, -1, -9, -2, -6, -7, -8, -5 }), 0.0001);
  }

  @Test public void testWikiData1() {
    assertEquals(0.5111, Kendall.tau(new double[] { 4, 10, 3, 1, 9, 2, 6, 7, 8, 5 }, new double[] { 5, 8, 6, 2, 10, 3, 9, 4, 7, 1 }), 0.0001);
  }

  @Test public void testWikiData2() {
    assertEquals(0.2, Kendall.tau(new double[] { 1, 2, 3, 4, 5 }, new double[] { 3, 4, 1, 2, 5 }), 0.0001);
  }

  @Test public void testWineData() {
    assertEquals(Kendall.tau(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, new double[] { 1, 3, 4, 5, 7, 8, 2, 9, 10, 6, 11 }), 0.6727, 0.0001);
  }

  @Test public void testZ() {
    assertEquals(2.88, new Kendall.Charectristics(11, 0.6727).z, 0.001);
  }
}
