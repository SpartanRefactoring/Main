package fluent.ly;

import static fluent.ly.___.positive;
import static fluent.ly.idiomatic.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.Contract;

import an.iterable;
import let.it;

/**
 * TODO Yossi Gil: document class
 *
 * @author Yossi Gil
 * @since 2017-04-12
 */
public interface the {
  first first = new first() {
    /* use default functions */};

  static char beforeLastOf(final String s, final int i) {
    return s.charAt(s.length() - i - 1);
  }

  /**
   * Retrieves the first element of a stream
   * 
   * @param <T> type of elements iterated over
   * @param ¢   an arbitrary iterable over this type
   * @return the first element of the parameter, supposing there is one. If there
   *         is not such element, the results are unpredictable.
   */
  static <T> T firstOf(final Iterable<T> ¢) {
    return ¢.iterator().next();
  }

  /**
   * Retrieves a prefix of a specified size of a stream
   * 
   * @param <T> type of elements iterated over
   * @param ts  an arbitrary iterable over this type
   * @param n   a non-negative integer
   * @return an array containing the first
   */
  static <T> ArrayList<T> first(final Iterable<T> ts, final int n) {
    final ArrayList<T> $ = new ArrayList<>();
    int i = 0;
    for (final T ¢ : ts) {
      $.add(¢);
      if (++i == n)
        break;
    }
    return $;
  }

  static <T> @Nullable T first(final Iterable<T> ts, final Predicate<T> p) {
    return the.first(ts.iterator(), p);
  }

  static <T> @Nullable T first(final Iterator<T> t, final Predicate<T> p) {
    while (t.hasNext()) {
      final T $ = t.next();
      if (p.test($))
        return $;
    }
    return null;
  }

  /**
   * Retrieves the first element of an array
   * 
   * @param <T> type of elements in the array
   * @param ¢   an arbitrary array of this type
   * @return the first element of the array if the array is of non-zero length,
   *         otherwise <code><b>null</b></code>
   */
  static <T> @Nullable T first(final T[] ¢) {
    return ¢.length > 0 ? ¢[0] : null;
  }

  @Contract("null -> null") static <T> T firstOf(final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(0);
  }

  @Contract("null -> null") static <T> @Nullable T thirdOf(final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(2);
  }

  static char firstOf(final String $) {
    return $.charAt(0);
  }

  static <T> T firstOf(final T[] ¢) {
    return ¢[0];
  }

  @SuppressWarnings("boxing") static int index(final int i, final int... is) {
    for (final Integer $ : range.from(0).to(is.length))
      if (is[$] == i)
        return $;
    return -1;
  }

  static int[] ints(final int... ¢) {
    return ¢;
  }

  @Contract(pure = true) static char ith(final String s, final int i) {
    return s.charAt(i);
  }

  /**
   * @param <T> JD
   * @param ¢   JD
   * @return last item in a list or <code><b>null</b></code> if the parameter is
   *         <code><b>null</b></code> or empty
   */
  static <T> T last(final List<T> ¢) {
    return eval(() -> ¢.get(¢.size() - 1)).unless(¢ == null || ¢.isEmpty());
  }

  static <T> Iterable<T> lastOf(final Iterable<T> ¢) {
    return () -> new Iterator<>() {
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

  @Contract("null -> null") static <T> T lastOf(final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(¢.size() - 1);
  }

  static char lastOf(final String ¢) {
    return beforeLastOf(¢, 0);
  }

  static <T> T lastOf(final T[] ¢) {
    return ¢[¢.length - 1];
  }

  /**
   * Computes the maximum of two or more integers.
   * 
   * @param a  some integer
   * @param is additional integers
   * @return largest of the parameters
   */
  static int max(final int a, final int... is) {
    int $ = a;
    for (final int ¢ : is)
      $ = Math.max($, ¢);
    return $;
  }

  /**
   * Computes the minimum of two or more integers
   * 
   * @param a  some integer
   * @param is additional
   * @return smallest of the parameters
   */
  static int min(final int a, final int... is) {
    int $ = a;
    for (final int ¢ : is)
      $ = Math.min($, ¢);
    return $;
  }

  static <T> @Nullable T nil() {
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

  static <T> T onlyOneOf(final List<T> ¢) {
    return ¢ == null || ¢.size() != 1 ? null : firstOf(¢);
  }

  /**
   * @param <T> JD
   * @param ¢   a list
   * @return last item in a list or <code><b>null</b></code> if the parameter is
   *         <code><b>null</b></code> or empty
   */
  static <T> T penultimateOf(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(¢.size() - 2);
  }

  static <T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
  }

  /**
   * Chop the first character of a string.
   * 
   * @param ¢ a non-<code><b>null</b></code> string of length at least one
   * @return <code>s</code> but without its first character.
   */
  static String rest(final String ¢) {
    assert ¢ != null;
    positive(¢.length());
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

  @Contract("null -> null") static <T> T secondOf(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(1);
  }

  /**
   * Computes the square of a given integer
   * 
   * @param ¢ some integer
   * @return square of the parameter
   */
  static int sqr(final int ¢) {
    return ¢ * ¢;
  }

  static <T> List<T> tailOf(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove(the.firstOf($));
    return $;
  }

  static String tailOf(final String ¢) {
    return ¢.substring(1);
  }

  static <T> T[] tailOf(final T[] ¢) {
    return Arrays.copyOfRange(¢, 1, ¢.length);
  }

  /**
   * @param <T> JD
   * @return <code><b>true</b></code> <i>iff</i> the receive is empty
   */
  static <T> Iterable<T> empty() {
    return iterable.over();
  }

  /**
   * wraps a value in a singleton iterator form
   * 
   * @param <T> JD
   * @param $   JD
   * @return parameter, but in a singleton iterator form
   */
  static <T> Iterator<T> singletonIterator(final T $) {
    return iterable.singleton($).iterator();
  }

  interface first {
    default it<String> of(final String ¢) {
      return new it<>(¢.substring(0, 1));
    }

    default <T> it<T> of(final T[] ¢) {
      return new it<>(¢[0]);
    }

    static char characterOf(final String ¢) {
      return the.beforeLastOf(¢, 0);
    }
  }
}
