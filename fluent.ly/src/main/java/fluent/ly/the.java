package fluent.ly;

import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.iterables.PureIterable;
import nano.ly.range;

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

  static <@Nullable T> T nil() {
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

  static <@Nullable T> T penultimateOf(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(¢.size() - 2);
  }

  static <@Nullable T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
  }

  static <@Nullable T> List<T> tailOf(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove(the.headOf($));
    return $;
  }

  static String tailOf(final String ¢) {
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

  @Contract("null -> null") @Nullable static <T> T headOf(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(0);
  }

  static char characterOf(@NotNull final String ¢) {
    return the.beforeLastOf(¢, 0);
  }

  @Contract(pure = true) static char ith(@NotNull final String s, final int i) {
    return s.charAt(i);
  }

  @Contract("null -> null") @Nullable static <@Nullable T> T lastOf(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(¢.size() - 1);
  }

  static char lastOf(@NotNull final String ¢) {
    return beforeLastOf(¢, 0);
  }

  static char beforeLastOf(@NotNull final String s, final int i) {
    return s.charAt(s.length() - i - 1);
  }

  @NotNull static <T> Iterable<T> lastOf(@NotNull final Iterable<T> ¢) {
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

  @Nullable static <T> T onlyOneOf(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.size() != 1 ? null : headOf(¢);
  }

  @Contract("null -> null") @Nullable static <T> T secondOf(@Nullable final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(1);
  }

  @NotNull static boolean[] rest(@SuppressWarnings("unused") @NotNull PureIterable.Sized<@Nullable String> s) {
    return null;
  }
}
