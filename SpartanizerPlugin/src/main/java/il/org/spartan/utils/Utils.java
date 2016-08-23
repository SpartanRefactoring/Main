package il.org.spartan.utils;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01 */
public enum Utils {
  ;
  /** Impose an ordering on type <code><b>boolean</b></code> by which
   * <code><b>true</b></code> is greater than <code><b>false</b></code>.
   * @param b1 JD
   * @param b2 JD
   * @return an integer that is negative, zero or positive depending on whether
   *         the first argument is less than, equal to, or greater than the
   *         second.
   * @see Comparable
   * @see Comparator */
  public static int compare(final boolean b1, final boolean b2) {
    return b1 == b2 ? 0 : b1 ? 1 : -1;
  }

  /** Deletes a specified element from an array, by reallocating an array whose
   * size is smaller by one and shifting the other elements down.
   * @param ts an arbitrary array
   * @param i position of element to be deleted
   * @return newly created array */
  public static <T> T[] delete(final T[] ts, final int i) {
    final T[] $ = Arrays.copyOf(ts, ts.length - 1);
    System.arraycopy(ts, i + 1, $, i, $.length - i);
    return $;
  }

  /** Convert variadic list of arguments into an array
   * @param os JD _
   * @return parameter, as an array. */
  public static Object[] objects(final Object... os) {
    return os;
  }

  public static <T> T onlyOne(final List<T> ts) {
    return ts == null || ts.size() != 1 ? null : ts.get(0);
  }

  /** @param ts a list
   * @return last item in a list or <code><b>null</b></code> if the parameter is
   *         <code><b>null</b></code> or empty */
  public static <T> T penultimate(final List<T> ts) {
    return ts == null || ts.size() < 2 ? null : ts.get(ts.size() - 2);
  }

  /** Determine whether an {@link Object} is penultimate in its {@link Block}.
   * @param o JD
   * @param os JD
   * @return <code><b>true</b></code> <i>iff</i> the an {@link Object} parameter
   *         occurs as the penultimate element of the {@link List} parameter */
  public static boolean penultimateIn(final Object o, final List<?> os) {
    return penultimate(os) == o;
  }

  /** Remove any duplicates that may be present in a given {@link List}
   * @param ts JD */
  public static <T> void removeDuplicates(final List<T> ts) {
    final Set<T> noDuplicates = new LinkedHashSet<>(ts);
    ts.clear();
    ts.addAll(noDuplicates);
  }

  /** Remove all occurrences of a given prefix from a given {@link String}.
   * @param s JD
   * @param prefix what should be removed
   * @return parameter after all such occurrences are removed. */
  public static String removePrefix(final String s, final String prefix) {
    for (String $ = s;; $ = $.substring(prefix.length()))
      if (!$.startsWith(prefix))
        return $;
  }

  /** Remove all occurrences of a given suffix from a given string.
   * @param s JD
   * @param suffix what should be removed
   * @return parameter after all such occurrences are removed. */
  public static String removeSuffix(final String s, final String suffix) {
    for (String $ = s;; $ = $.substring(0, $.length() - suffix.length()))
      if (!$.endsWith(suffix))
        return $;
  }

  /** Sorts an array
   * @param is what to sort
   * @return given array with elements in sorted order */
  public static int[] sort(final int[] is) {
    Arrays.sort(is);
    return is;
  }

  /** Determine whether a file name ends with any one of the supplied
   * extensions.
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions. */
  public static boolean suffixedBy(final File f, final Iterable<String> suffixes) {
    return suffixedBy(f.getName(), suffixes);
  }

  /** Determine whether a file name ends with any one of the supplied
   * extensions.
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions. */
  public static boolean suffixedBy(final File f, final String... suffixes) {
    return suffixedBy(f.getName(), suffixes);
  }

  /** Determine whether a string ends with any one of the supplied suffixes.
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes. */
  public static boolean suffixedBy(final String s, final Iterable<String> suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }

  /** Determine whether a string ends with any one of the supplied suffixes.
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes. */
  public static boolean suffixedBy(final String s, final String... suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
}
