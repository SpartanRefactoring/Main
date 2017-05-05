package il.org.spartan.utils;

import org.jetbrains.annotations.*;

import il.org.spatan.iteration.*;
import fluent.ly.*;

/** Computes the longest common prefix of the names of objects in a given set.
 * @author Yossi Gil
 * @param <T> type of objects in the set */
public class Prefix<T> {
  @Nullable public static String trim(final String prefix, @NotNull final String s) {
    for (@Nullable String $ = defaults.to(prefix, s);; $ = shorten($))
      if (s.startsWith($))
        return $;
  }
  private static String shorten(@NotNull final String ¢) {
    return ¢.substring(0, ¢.length() - 2);
  }
  @Nullable private static <T> String trim(@NotNull final Iterable<T> ts) {
    @Nullable String $ = null;
    for (final T ¢ : ts)
      $ = trim($, ¢ + "");
    return $;
  }

  @Nullable private final String prefix;

  public Prefix(@NotNull final Iterable<T> ts) {
    this.prefix = trim(ts);
  }
  public Prefix(final T[] ts) {
    this(Iterables.make(ts));
  }
  @NotNull public String trim(final T ¢) {
    return (¢ + "").substring(prefix.length());
  }
}
