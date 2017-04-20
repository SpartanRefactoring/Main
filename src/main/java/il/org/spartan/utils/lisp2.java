package il.org.spartan.utils;

import static il.org.spartan.Utils.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-19 */
public interface lisp2 extends lisp {
  /** @param o the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return whether all the operator are the same or false otherwise */
  static boolean areEqual(final Object o, final Object... os) {
    return !hasNull(o, os) && Stream.of(os).allMatch(λ -> λ == o);
  }

  static <T> List<T> chopLast(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove($.size() - 1);
    return $;
  }

  static String chopLast(final String ¢) {
    return ¢.substring(0, ¢.length() - 1);
  }

  static void removeFromList(final Iterable<Expression> items, final List<Expression> from) {
    items.forEach(from::remove);
  }

  static <T> void removeLast(final List<T> ¢) {
    ¢.remove(¢.size() - 1);
  }

  /** swaps two elements in an indexed list in given indexes, if they are legal
   * @param ts the indexed list
   * @param i1 the index of the first element
   * @param i2 the index of the second element
   * @return the list after swapping the elements */
  static <T> List<T> swap(final List<T> $, final int i1, final int i2) {
    if (i1 >= $.size() || i2 >= $.size())
      return $;
    final T t = $.get(i1);
    lisp.replace($, $.get(i2), i1);
    lisp.replace($, t, i2);
    return $;
  }
}
