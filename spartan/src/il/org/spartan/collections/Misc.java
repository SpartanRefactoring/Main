package il.org.spartan.collections;

import static fluent.ly.___.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jetbrains.annotations.*;

public class Misc {
  public static boolean compareWithStream(@NotNull final String s, @NotNull final InputStream is) {
    for (@NotNull final Scanner actual = new Scanner(s), expected = new Scanner(is);;) {
      if (actual.hasNext() != expected.hasNext())
        return false;
      if (!actual.hasNext())
        return true;
      @NotNull final String a = actual.nextLine().trim(), b = expected.nextLine().trim();
      if (!a.equals(b)) {
        System.err.println("a=" + a);
        System.err.println("b=" + b);
        return false;
      }
    }
  }

  @NotNull public static boolean[] complement(@NotNull final boolean[] bs) {
    @NotNull final boolean[] $ = new boolean[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = !bs[¢];
    return $;
  }

  @NotNull public static <T> T[] duplicate(@NotNull final T[] ¢) {
    @NotNull @SuppressWarnings("unchecked") final T[] $ = (T[]) java.lang.reflect.Array.newInstance(¢.getClass().getComponentType(), ¢.length);
    System.arraycopy(¢, 0, $, 0, ¢.length);
    return $;
  }

  @NotNull public static double[] ensureIndex(@NotNull final double[] as, final int i) {
    return i < as.length ? as : Arrays.copyOf(as, 1 + Math.max(i, as.length + (as.length >> 1)));
  }

  @NotNull public static int[] ensureIndex(@NotNull final int[] as, final int i) {
    return i < as.length ? as : Arrays.copyOf(as, 1 + Math.max(i, as.length + (as.length >> 1)));
  }

  @NotNull public static boolean[] toArray(@NotNull final List<Boolean> bs) {
    @NotNull final boolean[] $ = new boolean[bs.size()];
    for (int ¢ = 0; ¢ < bs.size(); ++¢)
      $[¢] = bs.get(¢).booleanValue();
    return $;
  }

  // public static<T> T[] toArray(T... ts) { return ts; }
  @NotNull public static <T> T[] toArray(@NotNull final T t, @NotNull final T... ts) {
    nonnull(t);
    nonnull(ts);
    @NotNull @SuppressWarnings("unchecked") final T[] $ = (T[]) Array.newInstance(t.getClass(), ts.length + 1);
    $[0] = t;
    for (int ¢ = 0; ¢ < ts.length; ++¢)
      $[¢ + 1] = ts[¢];
    return $;
  }
}
