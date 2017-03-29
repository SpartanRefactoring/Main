package il.org.spartan.utils;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.utils.range.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil {@code yogi@cs.technion.ac.il}
 * @since 2017-03-19 */
public interface lisp2 extends lisp {
  static void removeElFromList(final Iterable<Expression> items, final List<Expression> from) {
    items.forEach(from::remove);
  }

  static List<Expression> removeFirstElement(final List<Expression> ¢) {
    final List<Expression> $ = new ArrayList<>(¢);
    $.remove(first($));// remove first
    return $;
  }

  static <N extends ASTNode> List<? extends ASTNode> addRest(final List<ASTNode> $, final N n, final List<N> ns) {
    if (ns == null)
      return $;
    boolean add = false;
    for (final ASTNode x : ns)
      if (add)
        $.add(x);
      else
        add = x == n;
    return $;
  }

  @SuppressWarnings("boxing") static int index(final int i, final int... is) {
    for (final Integer $ : range.from(0).to(is.length))
      if (is[$] == i)
        return $;
    return -1;
  }

  static String nth(final int i, final Collection<?> os) {
    return lisp2.nth(i, os.size());
  }

  static String nth(final int i, final int n) {
    return nth(i + "", n + "");
  }

  static String nth(final String s, final String n) {
    return " #" + s + "/" + n;
  }

  static <T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
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
