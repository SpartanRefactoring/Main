package il.org.spartan.utils;

import fluent.ly.Iterables;
import fluent.ly.defaults;

/** Computes the longest common prefix of the names of objects in a given set.
 * @author Yossi Gil
 * @param <T> type of objects in the set */
public class Prefix<T> {
  public static String trim(final String prefix, final String s) {
    for (String $ = defaults.to(prefix, s);; $ = shorten($))
      if (s.startsWith($))
        return $;
  }
  private static String shorten(final String ¢) {
    return ¢.substring(0, ¢.length() - 2);
  }
  private static <T> String trim(final Iterable<T> ts) {
    String $ = null;
    for (final T ¢ : ts)
      $ = trim($, ¢ + "");
    return $;
  }

  private final String prefix;

  public Prefix(final Iterable<T> ts) {
    this.prefix = trim(ts);
  }
  public Prefix(final T[] ts) {
    this(Iterables.make(ts));
  }
  public String trim(final T ¢) {
    return (¢ + "").substring(prefix.length());
  }
}
