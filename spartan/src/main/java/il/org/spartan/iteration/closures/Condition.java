package il.org.spartan.iteration.closures;

import fluent.ly.forget;

/** An interface representing a boolean function taking single argument boolean.
 *
 * @param <Argument> Type of argument
 * @author Yossi Gil
 * @since 12/07/2007 */
public interface Condition<Argument> {
  /** Evaluate the function for the given input
   *
   * @param v Input argument
   * @return <code><b>true</b></code> if the predicate holds. */
  boolean holds(Argument v);

  enum Make {
    ;
    /** A an implementation of a filter that approves all objects.
     *
     * @author Yossi Gil
     * @since November 26, 2009
     * @param <T> type of elements in the iterable
     * @return the passed argument */
    public static <T> Condition<T> all() {
      return λ -> {
        forget.em(λ);
        return true;
      };
    }

    public static <T> Condition<T> and(final Condition<T> c1, final Condition<T> c2) {
      return λ -> c1.holds(λ) && c2.holds(λ);
    }

    public static <T> Condition<T> and(final Condition<T> c1, final Condition<T> c2, final Condition<T> c3) {
      return and(c1, and(c2, c3));
    }

    public static <T> Condition<T> and(final Condition<T> c1, final Condition<T> c2, final Condition<T> c3,
        final Condition<T> c4) {
      return and(c1, and(c2, c3, c4));
    }

    /** A an implementation of a filter that rejects all objects.
     *
     * @author Yossi Gil
     * @since November 26, 2009
     * @param <T> type of elements in the iterable
     * @return an empty iterable */
    public static <T> Condition<T> none() {
      return λ -> {
        forget.em(λ);
        return false;
      };
    }

    /** A an implementation of a filter that approves all non-null objects.
     *
     * @author Yossi Gil
     * @since November 26, 2009
     * @param <T> type of elements in the iterable
     * @return an iterable which is identical to the arugment, except that all null
     *         elements are removed. */
    public static <T> Condition<T> NonNull() {
      return λ -> λ != null;
    }

    public static <T> Condition<T> not(final Condition<T> c) {
      return λ -> !c.holds(λ);
    }

    public static <T> Condition<T> or(final Condition<T> c1, final Condition<T> c2) {
      return λ -> c1.holds(λ) || c2.holds(λ);
    }

    public static <T> Condition<T> or(final Condition<T> c1, final Condition<T> c2, final Condition<T> c3) {
      return or(c1, or(c2, c3));
    }
  }
}
