package il.org.spartan.xy;

import static fluent.ly.azzert.is;
import static il.org.spartan.misc.LinearAlgebra.isReal;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import an.array.of;
import fluent.ly.___;
import fluent.ly.azzert;
import il.org.spartan.collections.DoublesArray;

/** An interface for processing a sequence of X-Y points
 *
 * @author Yossi Gil
 * @since February 15, 2012 */
public interface XYProcessor {
  void done();

  void p(double x, double y);

  void p(double x, double y, double dy);

  void p(int x, int y);

  abstract class Filter extends Gatherer {
    @Override public final void gather(final double x, final double y, final double dy) {
      if (valid(x, y, dy))
        super.gather(x, y, dy);
    }

    public abstract boolean valid(double x, double y, double dy);
  }

  abstract class Gatherer extends Vacuous {
    private final DoublesArray xs = new DoublesArray();
    private final DoublesArray ys = new DoublesArray();
    private final DoublesArray dys = new DoublesArray();

    public final double[] dys() {
      return dys.toArray();
    }

    @Override public final void p(final double x, final double y, final double dy) {
      gather(x, y, dy);
    }

    public final double[] xs() {
      return xs.toArray();
    }

    public final double[] ys() {
      return ys.toArray();
    }

    protected void gather(final double x, final double y, final double dy) {
      xs.push(x);
      ys.push(y);
      dys.push(dy);
    }
  }

  class LogLog extends Wrapper {
    /** Instantiate {@link LogLog} .
     *
     * @param inner */
    public LogLog(final XYProcessor inner) {
      super(inner);
    }

    @Override public void p(final double x, final double y) {
      if (x > 0 && y > 0)
        super.p(log(x), log(y));
    }

    @Override public void p(final double x, final double y, final double dy) {
      if (x > 0 && y > 0)
        super.p(log(x), log(y), (log(y + dy) - log(y - dy)) / 2);
    }

    @Override public void p(final int x, final int y) {
      if (x > 0 && y > 0)
        super.p(log(x), log(y));
    }
  }

  class MaxErrorFilter extends Wrapper {
    private static double DEFAULT_FACTOR = 2;
    private double maxError = Double.NaN;
    private final double factor;

    public MaxErrorFilter(final XYProcessor inner) {
      this(inner, DEFAULT_FACTOR);
    }

    public MaxErrorFilter(final XYProcessor inner, final double factor) {
      super(inner);
      this.factor = factor;
    }

    @Override public void p(final double x, final double y, final double dy) {
      if (Double.isNaN(dy) || Double.isInfinite(dy))
        super.p(x, y, factor * maxError);
      if (Double.isNaN(maxError) || Double.isInfinite(maxError))
        maxError = dy;
      maxError = Math.max(maxError, dy);
      super.p(x, y, dy);
    }
  }

  class Minimizer extends Vacuous {
    private boolean improved;
    private double xMin = Double.NaN;
    private double yMin = Double.POSITIVE_INFINITY;
    private double dyMin = Double.NaN;

    public double dyMin() {
      return dyMin;
    }

    public boolean improved() {
      return improved;
    }

    @Override public void p(final double x, final double y, final double dy) {
      if (!(improved = y < yMin))
        return;
      xMin = x;
      yMin = y;
      dyMin = dy;
    }

    public double xMin() {
      return xMin;
    }

    public double yMin() {
      return yMin;
    }
  }

  class RealsOnly extends Filter {
    @Override public boolean valid(final double x, final double y, final double dy) {
      return isReal(x) && isReal(y) && isReal(dy);
    }

    @SuppressWarnings("static-method") public static class TEST {
      @Test public void feed() {
        final RealsOnly p = new RealsOnly();
        p.feed(of.doubles(Double.NaN, 1, 4, 3), of.doubles(0, Double.NEGATIVE_INFINITY, 5, 3),
            of.doubles(0, 1, 6, Double.NEGATIVE_INFINITY));
        azzert.that(p.xs().length, is(1));
        azzert.that(p.ys().length, is(1));
        azzert.that(p.dys().length, is(1));
        assertEquals(4.0, p.xs()[0], 1E-10);
        assertEquals(5.0, p.ys()[0], 1E-10);
        assertEquals(6.0, p.dys()[0], 1E-10);
      }

      @Test public void isDefinedFalse() {
        assert !isReal(Double.POSITIVE_INFINITY);
        assert !isReal(Double.NEGATIVE_INFINITY);
        assert !isReal(Math.log(0));
        assert !isReal(Double.NaN);
      }

      @Test public void isDefinedTrue() {
        assert isReal(1);
        assert isReal(0);
      }
    }
  }

  class SquareErrorWrapper extends Wrapper {
    /** Instantiate {@link SquareErrorWrapper} .
     *
     * @param inner */
    public SquareErrorWrapper(final XYProcessor inner) {
      super(inner);
    }

    @Override public void p(final double x, final double y) {
      if (y > 0)
        super.p(x, y, 1 / sqrt(y));
    }

    @Override public void p(final int x, final int y) {
      if (y > 0)
        super.p(x, y, 1 / sqrt(y));
    }
  }

  abstract class Vacuous implements XYProcessor {
    @Override public void done() {
      ___.nothing();
    }

    public Vacuous feed(final double xs[], final double ys[]) {
      assert xs.length == ys.length;
      final int n = Math.max(xs.length, ys.length);
      for (int ¢ = 0; ¢ < n; ++¢)
        p(xs[¢], ys[¢]);
      done();
      return this;
    }

    public Vacuous feed(final double xs[], final double ys[], final double dys[]) {
      assert xs.length == ys.length;
      assert ys.length == dys.length;
      final int n = Math.max(xs.length, ys.length);
      for (int ¢ = 0; ¢ < n; ++¢)
        p(xs[¢], ys[¢], dys[¢]);
      done();
      return this;
    }

    public Vacuous feed(final XYSeries s) {
      for (int ¢ = 0; ¢ < s.n(); ++¢)
        p(s.x[¢], s.y[¢], s.dy[¢]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final double ds[]) {
      for (int ¢ = 0; ¢ < ds.length; ++¢)
        if (ds[¢] != 0)
          p(¢, ds[¢]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final double ys[], final double dys[]) {
      for (int ¢ = 0; ¢ < ys.length; ++¢)
        if (ys[¢] != 0)
          p(¢, ys[¢], dys[¢]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final int as[]) {
      for (int ¢ = 0; ¢ < as.length; ++¢)
        if (as[¢] != 0)
          p(¢, as[¢]);
      done();
      return this;
    }

    @Override public void p(final double x, final double y) {
      p(x, y, 0);
    }

    @Override public void p(final double x, final double y, final double dy) {
      ___.nothing();
    }

    @Override public void p(final int x, final int y) {
      p(1. * x, 1. * y);
    }
  }

  abstract class Wrapper extends Vacuous {
    protected final XYProcessor inner;

    /** Instantiate {@link Wrapper} .
     *
     * @param inner */
    public Wrapper(final XYProcessor inner) {
      this.inner = inner;
    }

    @Override public void done() {
      inner.done();
    }

    @Override public void p(final double x, final double y) {
      inner.p(x, y);
    }

    @Override public void p(final double x, final double y, final double dy) {
      inner.p(x, y, dy);
    }

    @Override public void p(final int x, final int y) {
      inner.p(x, y);
    }
  }

  class XShift extends Wrapper {
    private final double xshift;

    public XShift(final double xshift, final XYProcessor inner) {
      super(inner);
      this.xshift = xshift;
    }

    @Override public void p(final double x, final double y) {
      super.p(x + xshift, y);
    }

    @Override public void p(final double x, final double y, final double dy) {
      super.p(x + xshift, y, dy);
    }

    @Override public void p(final int x, final int y) {
      super.p(x + xshift, y);
    }
  }
}
