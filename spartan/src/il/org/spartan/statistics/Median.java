/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.utils.Box.*;

import java.util.*;

import il.org.spartan.streotypes.*;
import il.org.spatan.iteration.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum Median {
  ;
  public static double destructiveMad(final double... ds) {
    final int n = ds.length;
    final double median = destructiveMedian(ds);
    final double $[] = new double[n];
    for (int i = 0; i < n; i++)
      $[i] = Math.abs(ds[i] - median);
    return destructiveMedian($);
  }

  public static double destructiveMedian(final double... ds) {
    Arrays.sort(ds);
    final int n = ds.length;
    return (ds[n / 2] + ds[(n - 1) / 2]) / 2;
  }

  public static double mad(final double... ds) {
    return destructiveMad(ds.clone());
  }

  public static double median(final double... ds) {
    return destructiveMedian(ds.clone());
  }

  public static double[] prune(final double... ds) {
    final List<Double> $ = new ArrayList<>();
    final double median = destructiveMedian(ds);
    final double mad = mad(ds);
    for (final double d : ds)
      if (median - 2 * mad <= d && d <= median + 2 * mad)
        $.add(box(d));
    return Iterables.toArray($);
  }
}
