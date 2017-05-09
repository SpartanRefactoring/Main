/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package fluent.ly;

import java.util.*;
import java.util.stream.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-23 */
public interface is {
  static <T> boolean empty(final Collection<T> ¢) {
    return ¢ == null || ¢.isEmpty();
  }
  static <T> boolean empty(final Iterable<T> ¢) {
    return ¢ == null || !¢.iterator().hasNext();
  }
  static boolean empty(final String ¢) {
    return ¢ == null || ¢.isEmpty();
  }
  static <T> boolean empty(final T[] ¢) {
    return ¢ == null || ¢.length == 0;
  }
  /** Determine if an item can be found in a list of values
   * @param < T > JD
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is found in the list */
  @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
    return Stream.of(ts).anyMatch(λ -> λ != null && λ.equals(candidate));
  }
  /** Determine whether an integer is a valid list index
   * @param <T> JD
   * @param i some integer
   * @param ts a list of things
   * @return <code><b>true</b></code> <i>iff</i> the index is valid index into
   *         the list. and it is the last one in it. */
  static <T> boolean inRange(final int i, final List<T> ts) {
    return i >= 0 && i < ts.size();
  }
  /** Determine if an integer can be found in a list of values
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list */
  @SafeVarargs @Contract(pure = true) static boolean intIsIn(final int candidate, final int... is) {
    for (final int ¢ : is)
      if (¢ == candidate)
        return true;
    return false;
  }
  static boolean nil(final Object ¢) {
    return ¢ == null;
  }
  /** Determine if an item is not included in a list of values
   * @param <T> JD
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is not found in the list */
  @SafeVarargs static <T> boolean out(final T candidate, final T... ts) {
    return !in(candidate, ts);
  }

  interface not {
    /** the candidate is not in ts */
    @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
      return !is.in(candidate, ts);
    }
  }
}
