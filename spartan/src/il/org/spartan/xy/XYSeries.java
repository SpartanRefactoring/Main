package il.org.spartan.xy;

import static il.org.spartan.misc.LinearAlgebra.*;
import static il.org.spatan.iteration.Iterables.*;

import org.jetbrains.annotations.*;

import il.org.spartan.misc.*;
import il.org.spartan.xy.XYProcessor.*;

/** /**
 * @author Yossi Gil
 * @since Mar 1, 2012 */
public class XYSeries {
  @NotNull public static XYSeries histogram(@NotNull final double[] y) {
    return new XYSeries(seq(y.length), y);
  }

  @NotNull public static XYSeries histogram(@NotNull final double[] y, final double[] dy) {
    return new XYSeries(seq(y.length), y, dy);
  }

  @NotNull public static XYSeries histogram(@NotNull final int[] y) {
    return histogram(LinearAlgebra.promote(y));
  }

  public final double[] x;
  public final double[] y;
  public final double[] dy;

  public XYSeries(final double[] x, @NotNull final double[] y) {
    this(x, y, product(0, y));
  }

  public XYSeries(final double[] x, final double[] y, final double[] dy) {
    this.x = x;
    this.y = y;
    this.dy = dy;
  }

  public XYSeries(@NotNull final Gatherer g) {
    this(g.xs(), g.ys(), g.dys());
  }

  @NotNull public XYSeries log() {
    @NotNull final XYProcessor.RealsOnly $ = new XYProcessor.RealsOnly();
    $.feed(LinearAlgebra.log(x), LinearAlgebra.log(y), dLogY());
    return new XYSeries($);
  }

  public int n() {
    return y.length;
  }

  @NotNull public XYSeries scale(final double newMaxY) {
    return scale(max(y), newMaxY);
  }

  @NotNull public XYSeries xshift(final double c) {
    return new XYSeries(add(c, x), y, dy);
  }

  @NotNull public XYSeries yshift(final double c) {
    return new XYSeries(x, add(c, y), dy);
  }

  @NotNull private double[] dLogY() {
    @NotNull final double $[] = new double[y.length];
    for (int ¢ = 0; ¢ < y.length; ++¢)
      $[¢] = (Math.log(y[¢] + dy[¢]) - Math.log(y[¢] - dy[¢])) / 2;
    return $;
  }

  @NotNull private XYSeries scale(final double oldMaxY, final double newMaxY) {
    for (int ¢ = 0; ¢ < n(); ++¢) {
      y[¢] = newMaxY * y[¢] / oldMaxY;
      dy[¢] = newMaxY * dy[¢] / oldMaxY;
    }
    return this;
  }
}