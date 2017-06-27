package il.org.spartan.collections;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class Misc {
  public static boolean compareWithStream(final String s, final InputStream is) {
    try (final Scanner actual = new Scanner(s); final Scanner expected = new Scanner(is);) {
      for (;;) {
      if (actual.hasNext() != expected.hasNext())
        return false;
      if (!actual.hasNext())
        return true;
      final String a = actual.nextLine().trim(), b = expected.nextLine().trim();
      if (!a.equals(b)) {
        System.err.println("a=" + a);
        System.err.println("b=" + b);
        return false;
      }
      }
    }
  }
  public static boolean[] complement(final boolean[] bs) {
    final boolean[] $ = new boolean[bs.length];
    for (int ¢ = 0; ¢ < bs.length; ++¢)
      $[¢] = !bs[¢];
    return $;
  }
  public static <T> T[] duplicate(final T[] ¢) {
    @SuppressWarnings("unchecked") final T[] $ = (T[]) java.lang.reflect.Array.newInstance(¢.getClass().getComponentType(), ¢.length);
    System.arraycopy(¢, 0, $, 0, ¢.length);
    return $;
  }
  public static double[] ensureIndex(final double[] as, final int i) {
    return i < as.length ? as : Arrays.copyOf(as, 1 + Math.max(i, as.length + (as.length >> 1)));
  }
  public static int[] ensureIndex(final int[] as, final int i) {
    return i < as.length ? as : Arrays.copyOf(as, 1 + Math.max(i, as.length + (as.length >> 1)));
  }
  public static boolean[] toArray(final List<Boolean> bs) {
    final boolean[] $ = new boolean[bs.size()];
    for (int ¢ = 0; ¢ < bs.size(); ++¢)
      $[¢] = bs.get(¢).booleanValue();
    return $;
  }
  // public static<T> T[] toArray(T... ts) { return ts; }
  public static <T> T[] toArray(final T t, final T... ts) {
    assert t != null;
    assert ts != null;
    @SuppressWarnings("unchecked") final T[] $ = (T[]) Array.newInstance(t.getClass(), ts.length + 1);
    $[0] = t;
    for (int ¢ = 0; ¢ < ts.length; ++¢)
      $[¢ + 1] = ts[¢];
    return $;
  }
}
