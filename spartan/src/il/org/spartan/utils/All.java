// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import il.org.spartan.streotypes.*;

/** A utility class to realize universally quantified checks of an entire
 * collection.
 * @author Yossi Gil, the Technion.
 * @since 21/06/2008 */
@Utility public enum All {
  ;
  /** Determine whether all elements in an {@link Iterable} collection are not
   * null.
   * @param <T> type of elements in the collection
   * @param ts an {@link Iterable} collection of elements to be tested
   * @return <code><b>true</b></code> <i>iff</i> <code>ts</code> is not
   *         <code><b>null</b></code> and all elements in it is not are not
   *         <code><b>null</b></code> */
  public static <T> boolean notNull(final Iterable<T> ts) {
    if (ts == null)
      return false;
    for (final T t : ts)
      if (t == null)
        return false;
    return true;
  }

  /** Determine whether all elements in an array are not null.
   * @param <T> type of elements in the array
   * @param ts an array of elements to be tested
   * @return <code><b>true</b></code> <i>iff</i> <code>ts</code> is not
   *         <code><b>null</b></code> and all elements in it are not
   *         <code><b>null</b></code>. */
  public static <T> boolean notNull(final T[] ts) {
    if (ts == null)
      return false;
    for (final T t : ts)
      if (t == null)
        return false;
    return true;
  }
}
