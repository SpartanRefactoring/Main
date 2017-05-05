package il.org.spartan.statistics;

import static fluent.ly.box.*;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility
public enum Median {
  ;
  public static double destructiveMad(@NotNull final double... ds) {
    final int n = ds.length;
    final double median = destructiveMedian(ds);
    @NotNull final double $[] = new double[n];
    for (int ¢ = 0; ¢ < n; ++¢)
      $[¢] = Math.abs(ds[¢] - median);
    return destructiveMedian($);
  }
  public static double destructiveMedian(@NotNull final double... ¢) {
    Arrays.sort(¢);
    return (¢[¢.length / 2] + ¢[(¢.length - 1) / 2]) / 2;
  }
  public static double mad(@NotNull final double... ¢) {
    return destructiveMad(¢.clone());
  }
  public static double median(@NotNull final double... ¢) {
    return destructiveMedian(¢.clone());
  }
  @NotNull public static double[] prune(@NotNull final double... ds) {
    @NotNull final List<Double> $ = new ArrayList<>();
    final double median = destructiveMedian(ds), mad = mad(ds);
    for (final double ¢ : ds)
      if (median - 2 * mad <= ¢ && ¢ <= median + 2 * mad)
        $.add(box(¢));
    return Iterables.toArray($);
  }
}
