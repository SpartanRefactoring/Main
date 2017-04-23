package nano.ly;

import static il.org.spartan.lisp.*;

import java.util.*;

import il.org.spartan.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
public interface the {
  @SuppressWarnings("boxing") static int index(final int i, final int... is) {
    for (final Integer $ : range.from(0).to(is.length))
      if (is[$] == i)
        return $;
    return -1;
  }

  static <T> T nil() {
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

  static <T> T penultimate(final List<T> ¢) {
    return ¢ == null || ¢.size() < 2 ? null : ¢.get(¢.size() - 2);
  }

  static <T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
  }

  static <T> List<T> rest(final List<T> ¢) {
    final List<T> $ = as.list(¢);
    $.remove(first($));
    return $;
  }

  static String rest(final String ¢) {
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
}
