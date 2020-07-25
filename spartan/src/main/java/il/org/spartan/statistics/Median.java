package il.org.spartan.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fluent.ly.Iterables;
import fluent.ly.idiomatic;
import il.org.spartan.streotypes.Utility;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum Median {
  ;
  public static double destructiveMad(final double... ds) {
    final int n = ds.length;
    final double median = destructiveMedian(ds), $[] = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.abs(ds[¢] - median);
    return destructiveMedian($);
  }

  public static double destructiveMedian(final double... ¢) {
    Arrays.sort(¢);
    return (¢[¢.length / 2] + ¢[(¢.length - 1) / 2]) / 2;
  }

  public static double mad(final double... ¢) {
    return destructiveMad(¢.clone());
  }

  public static double median(final double... ¢) {
    return destructiveMedian(¢.clone());
  }

  public static double[] prune(final double... ds) {
    final List<Double> $ = new ArrayList<>();
    final double median = destructiveMedian(ds), mad = mad(ds);
    for (final double ¢ : ds)
      if (median - 2 * mad <= ¢ && ¢ <= median + 2 * mad)
        $.add(idiomatic.box(¢));
    return Iterables.toArray($);
  }
}
