package il.org.spartan.statistics;

import static il.org.spatan.iteration.Iterables.*;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.external.*;
import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;
import fluent.ly.*;

/** Provides services for computing the Kendall's tau metric for similarity
 * between rankings.
 * @author Yossi Gil
 * @since Dec 6, 2009 */
@Utility public enum Kendall {
  ;
  static final boolean FAST = true;

  @NotNull public static Charectristics makeCharectristics(@NotNull final double xs[]) {
    return makeCharectristics(xs, seq(xs.length));
  }

  @NotNull public static Charectristics makeCharectristics(@NotNull final double xs[], @NotNull final double ys[]) {
    return new Charectristics(xs, ys);
  }

  public static double tau(@NotNull final double ys[]) {
    return tau(seq(ys.length), ys);
  }

  /** [[SuppressWarningsSpartan]] */
  public static double tau(@NotNull final double[] xs, @NotNull final double[] ys) {
    ___.require(xs.length == ys.length);
    return computeS(xs, ys, xs.length) / (double) pairs(xs.length);
  }

  /** Compute Kendall's tau coefficient for a ranking of the integers 0,...,n
   * @param xs arbitrary values of the first ranking
   * @param ys
   * @return the Kendall tau coefficient of these two rankings. */
  public static double tau(@NotNull final Iterable<Double> xs, @NotNull final Iterable<Double> ys) {
    return tau(Iterables.toArray(xs), Iterables.toArray(ys));
  }

  public static double tauB(@NotNull final double ys[]) {
    return tauB(seq(ys.length), ys);
  }

  public static double tauB(@NotNull final double[] xs, @NotNull final double[] ys) {
    ___.require(xs.length == ys.length);
    @NotNull final List<Double> $ = new ArrayList<>(), Ys = new ArrayList<>();
    for (int ¢ = 0; ¢ < xs.length; ++¢)
      if (!Double.isNaN(xs[¢]) && !Double.isNaN(ys[¢])) {
        $.add(box.it(xs[¢]));
        Ys.add(box.it(ys[¢]));
      }
    return tauB_pruned(Iterables.toArray($), Iterables.toArray(Ys));
  }

  /** [[SuppressWarningsSpartan]] */
  static int compueS(@NotNull final double[] xs, @NotNull final double[] ys) {
    ___.require(xs.length == ys.length);
    int $ = 0;
    for (int i = 0; i < xs.length; ++i)
      for (int j = i + 1; j < xs.length; ++j)
        if (xs[i] != xs[j] && ys[i] != ys[j])
          if (xs[i] > xs[j] == ys[i] > ys[j])
            ++$;
          else
            --$;
    return $;
  }

  /** [[SuppressWarningsSpartan]] */
  static int compueS(@NotNull final int[] xs, @NotNull final int[] ys) {
    ___.require(xs.length == ys.length);
    int $ = 0;
    for (int i = 0; i < xs.length; ++i)
      for (int j = i + 1; j < xs.length; ++j)
        if (xs[i] != xs[j] && ys[i] != ys[j])
          if (xs[i] > xs[j] == ys[i] > ys[j])
            ++$;
          else
            --$;
    return $;
  }

  static int pairs(final int ¢) {
    ___.nonnegative(¢);
    return ¢ * (¢ - 1) / 2;
  }

  static int sigma(@NotNull final double[] ¢) {
    final double[] $ = ¢.clone();
    Arrays.sort($);
    return sigmaSortedArray($);
  }

  static int sigmaSortedArray(@NotNull final double[] ds) {
    int $ = 0;
    for (int i = 0; i < ds.length;) {
      if (Double.isNaN(ds[i])) {
        ++i;
        continue;
      }
      int j = i;
      while (j < ds.length && ds[j] == ds[i])
        ++j;
      $ += pairs(j - i);
      i = j;
    }
    return $;
  }

  private static int computeS(final double[] xs, final double[] ys, final int n) {
    int $ = 0, nd = 0;
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j)
        if (xs[i] > xs[j] && ys[i] > ys[j] || xs[i] < xs[j] && ys[i] < ys[j])
          ++$;
        else if (xs[i] > xs[j] && ys[i] < ys[j] || xs[i] < xs[j] && ys[i] > ys[j])
          ++nd;
    return $ - nd;
  }

  private static double tauB_pruned(@NotNull final double[] xs, @NotNull final double[] ys) {
    ___.require(xs.length == ys.length);
    final int $ = xs.length, pairs = pairs($);
    return computeS(xs, ys, $) / Math.sqrt(1. * (pairs - sigma(xs)) * (pairs - sigma(ys)));
  }

  public static class Charectristics {
    private static int valid(@NotNull final double[] xs, final double[] ys) {
      int $ = 0;
      for (int ¢ = 0; ¢ < xs.length; ++¢)
        $ += as.bit(!Double.isNaN(xs[¢]) && !Double.isNaN(ys[¢]));
      return $;
    }

    @External public final double tau;
    @External public final int n;
    @External public final double z;

    public Charectristics(@NotNull final double xs[], @NotNull final double ys[]) {
      this(valid(xs, ys), Kendall.tauB(xs, ys));
    }

    public Charectristics(@NotNull final double xs[], final double ys[], final double tau) {
      this(valid(xs, ys), tau);
    }

    public Charectristics(final int n, final double tau) {
      this.tau = tau;
      this.n = n;
      z = 3 * n * tau * (n - 1) / 2 / Math.sqrt(n * (n - 1.0) * (2.0 * n + 5) / 2);
    }
  }
}
