package il.org.spartan.sequence;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.external.*;
import il.org.spartan.utils.*;
import il.org.spatan.iteration.*;
import fluent.ly.*;

/** @author Yossi Gil
 * @since 24 ביול 2011 */
public class Ergodic {
  @External(name = "from") static int FROM = 1;
  @External(name = "to") static int TO = 100;
  @External(name = "N") static int N = 20;

  public static void main(@NotNull final String[] args) {
    External.Introspector.extract(args, Ergodic.class);
    System.out.println(Separate.by(make(N, FROM, TO), " "));
    System.out.println(Separate.by(makeDouble(N, 0.01, 10), " "));
  }

  @NotNull public static int[] make(final int max, final int... is) {
    return makeInt(max, Iterables.toList(is));
  }

  @NotNull public static double[] makeDouble(final int max, final double... ds) {
    return makeDouble(max, Iterables.toList(ds));
  }

  static int countDiff(@NotNull final List<Integer> is, final float diff) {
    int $ = 0;
    for (int ¢ = 1; ¢ < is.size(); ++¢)
      if (diff(is, ¢) == diff)
        ++$;
    return $;
  }

  static int countDiffDouble(@NotNull final List<Double> ds, final double diff) {
    int $ = 0;
    for (int ¢ = 1; ¢ < ds.size(); ++¢)
      if (diffDouble(ds, ¢) == diff)
        ++$;
    return $;
  }

  static float maxDiff(@NotNull final List<Integer> is) {
    float $ = -1;
    for (int ¢ = 1; ¢ < is.size(); ++¢)
      if (valid(is, ¢) && diff(is, ¢) > $)
        $ = diff(is, ¢);
    return $;
  }

  static double maxDiffDouble(@NotNull final List<Double> ds) {
    double $ = -1;
    for (int ¢ = 1; ¢ < ds.size(); ++¢)
      if (diffDouble(ds, ¢) > $)
        $ = diffDouble(ds, ¢);
    return $;
  }

  private static float diff(@NotNull final List<Integer> is, final int i) {
    return !valid(is, i) ? -1 : (float) is.get(i).intValue() / is.get(i - 1).intValue();
  }

  private static double diffDouble(@NotNull final List<Double> ds, final int i) {
    return ds.get(i).doubleValue() / ds.get(i - 1).doubleValue();
  }

  @NotNull private static double[] makeDouble(final int i, @NotNull final List<Double> $) {
    for (;;) {
      if ($.size() >= i)
        break;
      @Nullable final Double d = selectDouble($);
      if (d == null)
        break;
      $.add(d);
    }
    return unbox.unbox($.toArray(new Double[$.size()]));
  }

  @NotNull private static int[] makeInt(final int n, @NotNull final List<Integer> $) {
    for (;;) {
      if ($.size() >= n)
        break;
      @Nullable final Integer i = select($);
      if (i == null)
        break;
      $.add(i);
    }
    return unbox.it($);
  }

  private static Integer mid(@NotNull final List<Integer> is, final int i) {
    return mid(is.get(i - 1).intValue(), is.get(i).intValue());
  }

  private static Integer mid(final long a, final long b) {
    return Integer.valueOf((int) Math.round(Math.sqrt(a * b)));
  }

  private static Double midDouble(final double a, final double d) {
    return Double.valueOf(Math.round(Math.sqrt(a * d)));
  }

  private static Double midDouble(@NotNull final List<Double> ds, final int i) {
    return midDouble(ds.get(i - 1).doubleValue(), ds.get(i).doubleValue());
  }

  private static Integer select(@NotNull final List<Integer> is) {
    @NotNull final List<Integer> $ = new ArrayList<>(is);
    Collections.sort($);
    final float maxDiff = maxDiff($);
    return maxDiff < 0 ? null : selectDiff($, maxDiff, new Random(0).nextInt(countDiff($, maxDiff)));
  }

  private static Integer selectDiff(@NotNull final List<Integer> is, final float maxDiff, final int nextInt) {
    for (int ¢ = 0, $ = 1; $ < is.size(); ++$)
      if (valid(is, $) && diff(is, $) == maxDiff) {
        if (¢ == nextInt)
          return mid(is, $);
        ++¢;
      }
    return null;
  }

  private static Double selectDiffDouble(@NotNull final List<Double> ds, final double maxDiff, final int nextInt) {
    for (int ¢ = 0, $ = 1; $ < ds.size(); ++$)
      if (diffDouble(ds, $) == maxDiff) {
        if (¢ == nextInt)
          return midDouble(ds, $);
        ++¢;
      }
    return null;
  }

  private static Double selectDouble(@NotNull final List<Double> ds) {
    @NotNull final List<Double> $ = new ArrayList<>(ds);
    Collections.sort($);
    final double maxDiff = maxDiffDouble($);
    return maxDiff < 0 ? null : selectDiffDouble($, maxDiff, new Random(0).nextInt(countDiffDouble($, maxDiff)));
  }

  private static boolean valid(@NotNull final List<Integer> is, final int i) {
    return is.get(i - 1).intValue() != is.get(i).intValue() - 1;
  }
}
