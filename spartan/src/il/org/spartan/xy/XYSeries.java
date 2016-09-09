package il.org.spartan.xy;

import static il.org.spartan.misc.LinearAlgebra.*;
import static il.org.spatan.iteration.Iterables.*;

import il.org.spartan.misc.*;
import il.org.spartan.xy.XYProcessor.*;

/** /**
 * @author Yossi Gil
 * @since Mar 1, 2012 */
public class XYSeries {
  public static XYSeries histogram(final double[] y) {
    return new XYSeries(seq(y.length), y);
  }

  public static XYSeries histogram(final double[] y, final double[] dy) {
    return new XYSeries(seq(y.length), y, dy);
  }

  public static XYSeries histogram(final int[] y) {
    return histogram(LinearAlgebra.promote(y));
  }

  public final double[] x;
  public final double[] y;
  public final double[] dy;

  public XYSeries(final double[] x, final double[] y) {
    this(x, y, product(0, y));
  }

  public XYSeries(final double[] x, final double[] y, final double[] dy) {
    this.x = x;
    this.y = y;
    this.dy = dy;
  }

  public XYSeries(final Gatherer g) {
    this(g.xs(), g.ys(), g.dys());
  }

  public XYSeries log() {
    final XYProcessor.RealsOnly p = new XYProcessor.RealsOnly();
    p.feed(LinearAlgebra.log(x), LinearAlgebra.log(y), dLogY());
    return new XYSeries(p);
  }

  public int n() {
    return y.length;
  }

  public XYSeries scale(final double newMaxY) {
    return scale(max(y), newMaxY);
  }

  public XYSeries xshift(final double c) {
    return new XYSeries(add(c, x), y, dy);
  }

  public XYSeries yshift(final double c) {
    return new XYSeries(x, add(c, y), dy);
  }

  private double[] dLogY() {
    final double $[] = new double[y.length];
    for (int i = 0; i < y.length; i++)
      $[i] = (Math.log(y[i] + dy[i]) - Math.log(y[i] - dy[i])) / 2;
    return $;
  }

  private XYSeries scale(final double oldMaxY, final double newMaxY) {
    for (int i = 0; i < n(); ++i) {
      y[i] = newMaxY * y[i] / oldMaxY;
      dy[i] = newMaxY * dy[i] / oldMaxY;
    }
    return this;
  }
}