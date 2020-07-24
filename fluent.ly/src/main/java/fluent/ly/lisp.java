package fluent.ly;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Contract;

/** @noinspection unused */
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

  /**
   * Retrieve next item in a list
   *
   * @param i  an index of specific item in a list
   * @param ts the indexed list
   * @return following item in the list, if such such an item exists, otherwise,
   *         the last node
   */
  static <T> T next(final int i, final List<T> ts) {
    return is.inRange(i + 1, ts) ? ts.get(i + 1) : the.lastOf(ts);
  }

  /**
   * Retrieve previous item in a list
   *
   * @param i  an index of specific item in a list
   * @param ts the indexed list
   * @return previous item in the list, if such an item exists, otherwise, the
   *         last node
   */
  static <T> T prev(final int i, final List<T> ts) {
    return ts.get(i < 1 ? 0 : i - 1);
  }

  /**
   * Replace the element of a specific index in a list
   *
   * @param ts      the indexed list
   * @param element the element to be added to the list
   * @param index   the index that should be replaced
   * @return the list after the replacement
   */
  @Contract("null, _, _ -> null") static <T> List<T> replace(final List<T> ts, final T element, final int index) {
    if (ts == null || index < 0 || index >= ts.size())
      return ts;
    ts.remove(index);
    ts.add(index, element);
    return ts;
  }

  /**
   * Replace the first element of a in a list
   *
   * @param ts      the indexed list
   * @param element the element to be added to the list
   * @return the list after the replacement
   */
  @Contract("null, _ -> null") static <T> List<T> replaceFirst(final List<T> ts, final T element) {
    return replace(ts, element, 0);
  }

  /**
   * Replace the last element of a in a list
   *
   * @param ts      the indexed list
   * @param element the element to be added to the list
   * @return the list after the replacement
   */
  @Contract("null, _ -> null") static <T> List<T> replaceLast(final List<T> ts, final T element) {
    return replace(ts, element, ts.size() - 1);
  }

  static <T> Iterable<T> rest2(final Iterable<T> ¢) {
    return the.lastOf(the.lastOf(¢));
  }

  /**
   * @param o  the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return whether all the operator are the same or false otherwise
   */
  static boolean areEqual(final Object o, final Object... os) {
    return !has.nil(o, os) && Stream.of(os).allMatch(λ -> λ == o);
  }

  static <T> List<T> chopLast(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove($.size() - 1);
    return $;
  }

  static String chopLast(final String ¢) {
    return ¢.substring(0, ¢.length() - 1);
  }

  static <T> void removeFromList(final Iterable<T> items, final List<T> from) {
    items.forEach(from::remove);
  }

  static <T> void removeLast(final List<T> ¢) {
    ¢.remove(¢.size() - 1);
  }

  /**
   * swaps two elements in an indexed list in given indexes, if they are legal
   *
   * @param ts the indexed list
   * @param i1 the index of the first element
   * @param i2 the index of the second element
   * @return the list after swapping the elements
   */
  static <T> List<T> swap(final List<T> $, final int i1, final int i2) {
    if (i1 >= $.size() || i2 >= $.size())
      return $;
    final T t = $.get(i1);
    replace($, $.get(i2), i1);
    replace($, t, i2);
    return $;
  }
}
