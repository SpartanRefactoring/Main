package il.org.spartan.statistics;

import static il.org.spatan.iteration.Iterables.*;

import java.util.*;

import il.org.spartan.external.*;
import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;

/** Provides services for computing the Kendall's tau metric for similarity
 * between rankings.
 * @author Yossi Gil
 * @since Dec 6, 2009 */
@Utility public enum Kendall {
  ;
  static final boolean FAST = true;

  public static Charectristics makeCharectristics(final double xs[]) {
    return makeCharectristics(xs, seq(xs.length));
  }

  public static Charectristics makeCharectristics(final double xs[], final double ys[]) {
    return new Charectristics(xs, ys);
  }

  public static double tau(final double ys[]) {
    return tau(seq(ys.length), ys);
  }

  public static double tau(final double[] xs, final double[] ys) {
    ____.require(xs.length == ys.length);
    final int n = xs.length;
    return computeS(xs, ys, n) / (double) pairs(n);
  }

  /** Compute Kendall's tau coefficient for a ranking of the integers 0,...,n
   * @param xs arbitrary values of the first ranking
   * @param ys
   * @return the Kendall tau coefficient of these two rankings. */
  public static double tau(final Iterable<Double> xs, final Iterable<Double> ys) {
    return tau(Iterables.toArray(xs), Iterables.toArray(ys));
  }

  public static double tauB(final double ys[]) {
    return tauB(seq(ys.length), ys);
  }

  public static double tauB(final double[] xs, final double[] ys) {
    ____.require(xs.length == ys.length);
    final List<Double> Xs = new ArrayList<>();
    final List<Double> Ys = new ArrayList<>();
    for (int i = 0; i < xs.length; i++)
      if (!Double.isNaN(xs[i]) && !Double.isNaN(ys[i])) {
        Xs.add(Box.it(xs[i]));
        Ys.add(Box.it(ys[i]));
      }
    return tauB_pruned(Iterables.toArray(Xs), Iterables.toArray(Ys));
  }

  private static double tauB_pruned(final double[] xs, final double[] ys) {
    ____.require(xs.length == ys.length);
    final int n = xs.length;
    final int pairs = pairs(n);
    return computeS(xs, ys, n) / Math.sqrt((double) (pairs - sigma(xs)) * (pairs - sigma(ys)));
  }

  static int compueS(final double[] xs, final double[] ys) {
    ____.require(xs.length == ys.length);
    int $ = 0;
    for (int i = 0; i < xs.length; i++)
      for (int j = i + 1; j < xs.length; j++) {
        final double xi = xs[i], xj = xs[j], yi = ys[i], yj = ys[j];
        if (xi == xj || yi == yj)
          continue;
        if (xi > xj == yi > yj)
          ++$;
        else
          --$;
      }
    return $;
  }

  static int compueS(final int[] xs, final int[] ys) {
    ____.require(xs.length == ys.length);
    int $ = 0;
    for (int i = 0; i < xs.length; i++)
      for (int j = i + 1; j < xs.length; j++) {
        final int xi = xs[i], xj = xs[j], yi = ys[i], yj = ys[j];
        if (xi == xj || yi == yj)
          continue;
        if (xi > xj == yi > yj)
          ++$;
        else
          --$;
      }
    return $;
  }

  static int pairs(final int n) {
    ____.nonnegative(n);
    return n * (n - 1) / 2;
  }

  static int sigma(final double[] ds) {
    final double[] copy = ds.clone();
    Arrays.sort(copy);
    return sigmaSortedArray(copy);
  }

  static int sigmaSortedArray(final double[] ds) {
    int $ = 0;
    for (int i = 0; i < ds.length;) {
      if (Double.isNaN(ds[i])) {
        i++;
        continue;
      }
      int j = i;
      while (j < ds.length && ds[j] == ds[i])
        j++;
      $ += pairs(j - i);
      i = j;
    }
    return $;
  }

  private static int computeS(final double[] xs, final double[] ys, final int n) {
    int nc = 0;
    int nd = 0;
    for (int i = 0; i < n; i++)
      for (int j = i + 1; j < n; j++)
        if (xs[i] > xs[j] && ys[i] > ys[j] || xs[i] < xs[j] && ys[i] < ys[j])
          nc++;
        else if (xs[i] > xs[j] && ys[i] < ys[j] || xs[i] < xs[j] && ys[i] > ys[j])
          nd++;
    return nc - nd;
  }

  public static class Charectristics {
    private static int valid(final double[] xs, final double[] ys) {
      int $ = 0;
      for (int i = 0; i < xs.length; i++)
        $ += As.binary(!Double.isNaN(xs[i]) && !Double.isNaN(ys[i]));
      return $;
    }

    @External public final double tau;
    @External public final int n;
    @External public final double z;

    public Charectristics(final double xs[], final double ys[]) {
      this(valid(xs, ys), Kendall.tauB(xs, ys));
    }

    public Charectristics(final double xs[], final double ys[], final double tau) {
      this(valid(xs, ys), tau);
    }

    public Charectristics(final int n, final double tau) {
      this.tau = tau;
      this.n = n;
      z = 3 * tau * n * (n - 1) / 2 / Math.sqrt(n * (n - 1.0) * (2.0 * n + 5) / 2);
    }
  }
}
