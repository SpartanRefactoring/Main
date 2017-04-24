package nano.ly;

import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

import il.org.spartan.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface the {
  @SuppressWarnings("boxing") static int index(final int i, final int... is) {
    for (final Integer $ : range.from(0).to(is.length))
      if (is[$] == i)
        return $;
    return -1;
  }

  static <T> T nil() {
    return null;
  }

  static String nth(final int i, final Collection<?> os) {
    return the.nth(i, os.size());
  }

  static String nth(final int i, final int n) {
    return nth(i + "", n + "");
  }

  static String nth(final String s, final String n) {
    return " #" + s + "/" + n;
  }

  static <T> T penultimate(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(¢.size() - 2);
  }

  static <T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
  }

  static <T> List<T> rest(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove(the.first($));
    return $;
  }

  static String rest(final String ¢) {
    return ¢.substring(1);
  }

  static <T> List<T> rest(final T t, final Iterable<T> ts) {
    boolean add = false;
    final List<T> $ = an.empty.list();
    for (final T x : ts)
      if (add)
        $.add(x);
      else
        add = x == t;
    return $;
  }

  @Contract("null -> null") @Nullable static <T> T first(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(0);
  }

  static char first(@NotNull final String ¢) {
    return the.last(¢, 0);
  }

  @Contract(pure = true) static char first(@NotNull final String s, final int i) {
    return s.charAt(i);
  }

  @Contract("null -> null") @Nullable static <@Nullable T> T last(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(¢.size() - 1);
  }

  static char last(@NotNull final String ¢) {
    return last(¢, 0);
  }

  static char last(@NotNull final String s, final int i) {
    return s.charAt(s.length() - i - 1);
  }

  @NotNull static <T> Iterable<T> rest(@NotNull final Iterable<T> ¢) {
    return () -> new Iterator<T>() {
      final Iterator<T> $ = ¢.iterator();
      {
        $.next();
      }

      @Override public boolean hasNext() {
        return $.hasNext();
      }

      @Override public T next() {
        return $.next();
      }
    };
  }

  @Nullable static <T> T onlyOne(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.size() != 1 ? null : first(¢);
  }

  @Contract("null -> null") @Nullable static <T> T second(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(1);
  }
}
