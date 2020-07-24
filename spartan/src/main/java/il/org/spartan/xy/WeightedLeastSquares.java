package il.org.spartan.xy;

import static il.org.spartan.statistics.MomentUtils.sqr;
import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

import org.junit.Assert;
import org.junit.Test;

import an.array.of;
import il.org.spartan.utils.Separate;

/** @author Yossi Gil
 * @since February 22, 2012 */
public class WeightedLeastSquares extends XYProcessor.Vacuous {
  protected static boolean isMissing(final double x) {
    return isInfinite(x) || isNaN(x);
  }

  protected int n;
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
    for (int ¢ = 0; ¢ < x.length; ++¢)
      $[¢] = eval(x[¢]);
    return $;
  }

  public int n() {
    return n;
  }

  @Override public void p(final double x, final double y) {
    p(x, y, 1);
  }

  @Override public void p(final double x, final double y, final double dy) {
    if (isMissing(x) || isMissing(y) || isMissing(dy) || dy == 0)
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
    return Separate.byNewLines("𝑛=" + n(), "𝛼=" + alpha(), "𝛽=" + beta(), "𝑟²=" + r2(), "𝑟=" + r(),
        "H=" + xs.entropy());
  }

  public static class TEST {
    final WeightedLeastSquares l = new WeightedLeastSquares();
    {
      l.feed(of.doubles(1., 2, 3), of.doubles(5., 7, 9), of.doubles(1., 1, 1));
    }

    @Test public void alpha() {
      Assert.assertEquals(2, l.alpha(), 1E-5);
    }

    @Test public void beta() {
      Assert.assertEquals(3, l.beta(), 1E-5);
    }

    @Test public void eval() {
      Assert.assertEquals(21, l.eval(9), 1E-5);
    }
  }
}
