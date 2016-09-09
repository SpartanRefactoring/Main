/**
 *
 */
package il.org.spartan.xy;

import static il.org.spartan.misc.LinearAlgebra.*;
import static il.org.spatan.iteration.Iterables.*;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.collections.*;
import il.org.spartan.utils.*;

/** An interface for processing a sequence of X-Y points
 * @author Yossi Gil
 * @since February 15, 2012 */
public interface XYProcessor {
  void done();

  void p(double x, double y);

  void p(double x, double y, double dy);

  void p(int x, int y);

  public static abstract class Filter extends Gatherer {
    @Override public final void gather(final double x, final double y, final double dy) {
      if (valid(x, y, dy))
        super.gather(x, y, dy);
    }

    public abstract boolean valid(final double x, final double y, final double dy);
  }

  public static abstract class Gatherer extends Vacuous {
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

  public static class LogLog extends Wrapper {
    /** Instantiate {@link LogLog}.
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

  public static class MaxErrorFilter extends Wrapper {
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

  public static class Minimizer extends Vacuous {
    private boolean improved = false;
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

  public static class RealsOnly extends Filter {
    @Override public boolean valid(final double x, final double y, final double dy) {
      return isReal(x) && isReal(y) && isReal(dy);
    }

    @SuppressWarnings({ "static-method" }) //
    public static class TEST {
      @Test public void feed() {
        final RealsOnly p = new RealsOnly();
        p.feed(//
            doubles(Double.NaN, 1, 4, 3), //
            doubles(0, Double.NEGATIVE_INFINITY, 5, 3), //
            doubles(0, 1, 6, Double.NEGATIVE_INFINITY)//
        );
        assertEquals(1, p.xs().length);
        assertEquals(1, p.ys().length);
        assertEquals(1, p.dys().length);
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

  public static class SquareErrorWrapper extends Wrapper {
    /** Instantiate {@link SquareErrorWrapper}.
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

  public abstract static class Vacuous implements XYProcessor {
    @Override public void done() {
      ____.nothing();
    }

    public Vacuous feed(final double xs[], final double ys[]) {
      assert xs.length == ys.length;
      final int n = Math.max(xs.length, ys.length);
      for (int i = 0; i < n; i++)
        p(xs[i], ys[i]);
      done();
      return this;
    }

    public Vacuous feed(final double xs[], final double ys[], final double dys[]) {
      assert xs.length == ys.length;
      assert ys.length == dys.length;
      final int n = Math.max(xs.length, ys.length);
      for (int i = 0; i < n; i++)
        p(xs[i], ys[i], dys[i]);
      done();
      return this;
    }

    public Vacuous feed(final XYSeries s) {
      for (int i = 0; i < s.n(); i++)
        p(s.x[i], s.y[i], s.dy[i]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final double ds[]) {
      for (int i = 0; i < ds.length; i++)
        if (ds[i] != 0)
          p(i, ds[i]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final double ys[], final double dys[]) {
      for (int i = 0; i < ys.length; i++)
        if (ys[i] != 0)
          p(i, ys[i], dys[i]);
      done();
      return this;
    }

    public Vacuous feedHistogram(final int as[]) {
      for (int i = 0; i < as.length; i++)
        if (as[i] != 0)
          p(i, as[i]);
      done();
      return this;
    }

    @Override public void p(final double x, final double y) {
      p(x, y, 0);
    }

    @Override public void p(@SuppressWarnings("unused") final double x, @SuppressWarnings("unused") final double y,
        @SuppressWarnings("unused") final double dy) {
      ____.nothing();
    }

    @Override public void p(final int x, final int y) {
      p((double) x, (double) y);
    }
  }

  public abstract static class Wrapper extends Vacuous {
    protected final XYProcessor inner;

    /** Instantiate {@link Wrapper}.
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

  public static class XShift extends Wrapper {
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
