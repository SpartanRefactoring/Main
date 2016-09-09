/**
 *
 */
package il.org.spartan.xy;

import static il.org.spartan.statistics.MomentUtils.*;
import static il.org.spatan.iteration.Iterables.*;
import static java.lang.Double.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since February 22, 2012 */
public class WeightedLeastSquares extends XYProcessor.Vacuous {
  protected static boolean isNumber(final double x) {
    return !isInfinite(x) && !isNaN(x);
  }

  protected int n = 0;
  protected final WeightedDisribution xs = new WeightedDisribution();
  protected final WeightedDisribution ys = new WeightedDisribution();
  protected final WeightedDisribution xys = new WeightedDisribution();

  public double alpha() {
    return cov() / xs.var();
  }

  public double beta() {
    return ys.mean() - alpha() * xs.mean();
  }

  public void clear() {
    xs.clear();
    ys.clear();
    xys.clear();
    n = 0;
  }

  public double cov() {
    return xys.mean() - xs.mean() * ys.mean();
  }

  public double eval(final double x) {
    return beta() + x * alpha();
  }

  public double[] eval(final double[] x) {
    final double[] $ = new double[x.length];
    for (int i = 0; i < x.length; i++)
      $[i] = eval(x[i]);
    return $;
  }

  public int n() {
    return n;
  }

  @Override public void p(final double x, final double y) {
    p(x, y, 1);
  }

  @Override public void p(final double x, final double y, final double dy) {
    if (!isNumber(x) || !isNumber(y) || !isNumber(dy) || dy == 0)
      return;
    xs.record(x, 1 / dy);
    ys.record(y, 1 / dy);
    xys.record(x * y, 1 / dy);
    ++n;
  }

  public double r() {
    return cov() / Math.sqrt(xs.var() * ys.var());
  }

  public double r2() {
    return sqr(r());
  }

  @Override public String toString() {
    return Separate.byNewLines("𝑛=" + n(), "𝛼=" + alpha(), "𝛽=" + beta(), "𝑟²=" + r2(), "𝑟=" + r(), "H=" + xs.entropy());
  }

  public static class TEST {
    final WeightedLeastSquares l = new WeightedLeastSquares();
    {
      l.feed(doubles(1, 2, 3), doubles(5, 7, 9), doubles(1, 1, 1));
    }

    @Test public void alpha() {
      assertEquals(2, l.alpha(), 1E-5);
    }

    @Test public void beta() {
      assertEquals(3, l.beta(), 1E-5);
    }

    @Test public void eval() {
      assertEquals(21, l.eval(9), 1E-5);
    }
  }
}
