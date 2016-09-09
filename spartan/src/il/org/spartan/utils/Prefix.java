package il.org.spartan.utils;

import il.org.spatan.iteration.*;

/** Computes the longest common prefix of the names of objects in a given set.
 * @author Yossi Gil
 * @param <T> type of objects in the set */
public class Prefix<T> {
  public static String trim(final String prefix, final String s) {
    for (String $ = Defaults.to(prefix, s);; $ = shorten($))
      if (s.startsWith($))
        return $;
  }

  private static String shorten(final String s) {
    return s.substring(0, s.length() - 2);
  }

  private static <T> String trim(final Iterable<T> ts) {
    String $ = null;
    for (final T t : ts)
      $ = trim($, t.toString());
    return $;
  }

  private final String prefix;

  public Prefix(final Iterable<T> ts) {
    this.prefix = trim(ts);
  }

  public Prefix(final T[] ts) {
    this(Iterables.make(ts));
  }

  public String trim(final T t) {
    return t.toString().substring(prefix.length());
  }
}
