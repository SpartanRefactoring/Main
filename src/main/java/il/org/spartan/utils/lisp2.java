package il.org.spartan.utils;

import java.util.*;

import il.org.spartan.*;
import org.jetbrains.annotations.NotNull;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-19 */
public interface lisp2 extends lisp {
  @NotNull static String nth(final int i, @NotNull final Collection<?> os) {
    return lisp2.nth(i, os.size());
  }

  @NotNull static String nth(final int i, final int n) {
    return nth(i + "", n + "");
  }

  @NotNull static String nth(final String s, final String n) {
    return " #" + s + "/" + n;
  }

  /** swaps two elements in an indexed list in given indexes, if they are legal
   * @param ts the indexed list
   * @param i1 the index of the first element
   * @param i2 the index of the second element
   * @return the list after swapping the elements */
  @NotNull static <T> List<T> swap(@NotNull final List<T> $, final int i1, final int i2) {
    if (i1 < $.size() && i2 < $.size()) {
      final T t = $.get(i1);
      lisp.replace($, $.get(i2), i1);
      lisp.replace($, t, i2);
    }
    return $;
  }
}
