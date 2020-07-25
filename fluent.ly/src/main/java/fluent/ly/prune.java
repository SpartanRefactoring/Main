/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package fluent.ly;

import static il.org.spartan.Utils.cantBeNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A <b>Utility class</b> providing functions to remove <code><b>null</b></code>
 * elements from arrays and iterable collections. For example, to process the
 * non-<code><b>null</b></code> elements of an array:
 *
 * <pre>
 * void f(String ss[]) {
 *     for (String s: Prune.nulls(ss))
 *     		// ... s is not null.
 * }
 * </pre>
 *
 * @author Yossi Gil
 * @since 27/08/2008
 */
public enum prune {
  ;
  private static String[] asArrray(final List<String> $) {
    return cantBeNull($.toArray(new String[0]));
  }

  /**
   * Prune <code><b>null</b></code> elements from a given collection.
   *
   * @param <T> JD
   * @param <C> JD
   * @param $   JD
   */
  public static <T, C extends Collection<T>> C nils(final C $) {
    for (final var ¢ = $.iterator(); ¢.hasNext();)
      if (¢.next() == null)
        ¢.remove();
    return $;
  }

  /**
   * Prune <code><b>null</b></code> elements from a given collection.
   *
   * @param <T> type of elements in the collection.
   * @param ts  a collection of values.
   * @return a new collection, containing only those non- <code><b>null</b></code>
   *         elements of the parameter, and in the same order. No
   *         <code><b>null</b></code> elements are present on this returned
   *         collection.
   */
  public static <T> List<T> nils(final Iterable<T> ts) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return $;
  }

  /**
   * Prune <code><b>null</b></code> elements from a given array.
   *
   * @param <T> type of elements in the array.
   * @param ts  an array of values.
   * @return a new array, containing precisely those non- <code><b>null</b></code>
   *         elements of the parameter, and in the same order. No
   *         <code><b>null</b></code> elements are present on this returned
   *         collection.
   */
  public static <T> T[] nulls(final T[] ts) {
    final List<T> $ = new ArrayList<>();
    for (final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return cantBeNull($.toArray(shrink(ts)));
  }

  /**
   * Shrink an array size to zero.
   *
   * @param <T> type of elements in the input array.
   * @param ¢   an array of values.
   * @return an array of size 0 of elements of type <code>T</code>.
   */
  static <T> T[] shrink(final T[] ¢) {
    return Arrays.copyOf(¢, 0);
  }

  @SafeVarargs public static <T> String[] whites(final T... ts) {
    final List<String> $ = new ArrayList<>();
    for (final T ¢ : ts)
      if (¢ != null)
        accumulate.to($).add((¢ + "").trim());
    return asArrray($);
  }
}
