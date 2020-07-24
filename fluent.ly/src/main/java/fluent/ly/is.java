/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package fluent.ly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Contract;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-23 */
public interface is {
  static <T> Iterable<T> empty(@SuppressWarnings("unused") final Class<T> __) {
    return new ArrayList<>();
  }
  static <T> boolean empty(final Collection<T> ¢) {
    return ¢ == null || ¢.isEmpty();
  }
  /** Determines whether an iterable has any values.
   * @param <T> type of elements iterated over
   * @param ¢ an arbitrary iterable over this type
   * @return <code><b>true</b></code> <em>if an only if</em> the iterable is
   *         empty. */
  static <T> boolean empty(final Iterable<T> ¢) {
    return ¢ == null || !¢.iterator().hasNext();
  }
  static boolean empty(final String ¢) {
    return ¢ == null || ¢.isEmpty();
  }
  static <T> boolean empty(final T[] ¢) {
    return ¢ == null || ¢.length == 0;
  }
  /** @param ¢ JD */
  static is.FoundHandleForInt found(final int ¢) {
    return new FoundHandleForInt(¢);
  }
  /** @param <T> JD
   * @param ¢ JD */
  static <T> is.FoundHandleForT<T> found(final T ¢) {
    return new is.FoundHandleForT<>(¢);
  }
  /** Determine if an item can be found in a list of values
   * @param < T > JD
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is found in the list */
  @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
    return Stream.of(ts).anyMatch(λ -> λ != null && λ.equals(candidate));
  }
  static String indefinite(final String className) {
    final String $ = cCamelCase.components(className)[0];
    final char openingLetter = the.firstOf($);
    return English.isAcronym($) ? indefinite(English.pronounce(openingLetter)) : //
        (is.intIsIn(openingLetter, 'i', 'e', 'o', 'u', 'y') ? "an" : "a") + " " + className;
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
  /** @param os JD */
  static boolean isEmpty(final Iterable<?> os) {
    for (final Object name2 : os)
      if (name2 != null)
        return false;
    return true;
  }
  /** Determine whether an {@link Object} is the last in a {@link List} .
   * @param o JD
   * @param os JD
   * @return <code><b>true</b></code> <i>iff</i> the {@link Object} parameter is
   *         the same as the last element of the {@link List} parameter */
  static boolean lastIn(final Object o, final List<?> os) {
    return the.last(os) == o;
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

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  class FoundHandleForInt {
    final int candidate;

    /** Instantiates this class.
     * @param candidate what to search for */
    public FoundHandleForInt(final int candidate) {
      this.candidate = candidate;
    }
    /** Determine if an integer can be found in a list of values
     * @param is where to search
     * @return true if the the item is found in the list */
    @SafeVarargs public final boolean in(final int... is) {
      for (final int ¢ : is)
        if (¢ == candidate)
          return true;
      return false;
    }
  }

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @param <T> JD
   * @since 2016 */
  class FoundHandleForT<T> {
    final T candidate;

    /** Instantiates this class.
     * @param candidate what to search for */
    public FoundHandleForT(final T candidate) {
      this.candidate = candidate;
    }
    /** Determine if an integer can be found in a list of values
     * @param ts where to search
     * @return true if the the item is found in the list */
    @SafeVarargs public final boolean in(final T... ts) {
      for (final T ¢ : ts)
        if (¢ != null && ¢.equals(candidate))
          return true;
      return false;
    }
  }

  interface not {
    /** the candidate is not in ts */
    @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
      return !is.in(candidate, ts);
    }
  }
}
