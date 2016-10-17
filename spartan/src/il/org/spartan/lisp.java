package il.org.spartan;

import static il.org.spartan.Utils.*;

import java.util.*;

public interface lisp {
  static <T> List<T> chop(final List<T> ¢) {
    if (¢.isEmpty())
      return null;
    ¢.remove(0);
    return ¢;
  }

  static <T> List<T> cons(final T first, final List<T> rest) {
    rest.add(0, first);
    return rest;
  }

  static <T> T first(final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : first(¢);
  }

  static char first(final String ¢) {
    return last(¢, 0);
  }

  static char first(final String s, final int i) {
    return s.charAt(i);
  }

  /** Determine if an integer can be found in a list of values
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list */
  @SafeVarargs static boolean intIsIn(final int candidate, final int... is) {
    for (final int ¢ : is)
      if (¢ == candidate)
        return true;
    return false;
  }

  static <T> T last(final List<T> ¢) {
    return ¢ == null || ¢.isEmpty() ? null : ¢.get(¢.size() - 1);
  }

  static char last(final String ¢) {
    return last(¢, 0);
  }

  static char last(final String s, final int i) {
    return s.charAt(s.length() - i - 1);
  }

  /** Retrieve next item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return following item in the list, if such such an item exists, otherwise,
   *         the last node */
  static <T> T next(final int i, final List<T> ts) {
    return !inRange(i + 1, ts) ? last(ts) : ts.get(i + 1);
  }

  static <T> T onlyOne(final List<T> ¢) {
    return ¢ == null || ¢.size() != 1 ? null : first(¢);
  }

  /** Determine if an item is not included in a list of values
   * @param < T > JD
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is not found in the list */
  @SafeVarargs static <T> boolean out(final T candidate, final T... ts) {
    return !in(candidate, ts);
  }

  /** Retrieve previous item in a list
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return previous item in the list, if such an item exists, otherwise, the
   *         last node */
  static <T> T prev(final int i, final List<T> ts) {
    return ts.get(i < 1 ? 0 : i - 1);
  }

  /** Replace the element of a specific index in a list
   * @param ts the indexed list
   * @param element the element to be added to the list
   * @param index the index that should be replaced
   * @return the list after the replacement */
  static <T> List<T> replace(final List<T> ts, final T element, final int index) {
    if (ts != null && index >= 0 && index < ts.size()) {
      ts.remove(index);
      ts.add(index, element);
    }
    return ts;
  }

  /** Replace the first element of a in a list
   * @param ts the indexed list
   * @param element the element to be added to the list
   * @return the list after the replacement */
  static <T> List<T> replaceFirst(final List<T> ts, final T element) {
    return replace(ts, element, 0);
  }

  /** Replace the last element of a in a list
   * @param ts the indexed list
   * @param element the element to be added to the list
   * @return the list after the replacement */
  static <T> List<T> replaceLast(final List<T> ts, final T element) {
    return replace(ts, element, ts.size() - 1);
  }

  static <T> Iterable<T> rest(final Iterable<T> ¢) {
    return () -> {
      return new Iterator<T>() {
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
    };
  }

  static <T> Iterable<T> rest2(final Iterable<T> ¢) {
    return rest(rest(¢));
  }

  static <T> T second(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(1);
  }
}
