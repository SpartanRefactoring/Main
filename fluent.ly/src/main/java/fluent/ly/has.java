/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package fluent.ly;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A collection of <code><b>static</b></code> functions for converting from one
 * aggregate type to another.
 *
 * @author Yossi Gil
 * @since Jul 8, 2014
 */
public enum has {
  ;
  /**
   * Retrieve next item in a list
   * 
   * @param <T> JD
   * @param i   an index of specific item in a list
   * @param ts  the indexed list
   * @return following item in the list, if such such an item exists, otherwise,
   *         the last node
   */
  public static <T> T next(final int i, final List<T> ts) {
    return !is.inRange(i + 1, ts) ? the.last(ts) : ts.get(i + 1);
  }

  /**
   * Determine whether a <code><b>null</b></code> occurs in a sequence of objects
   * 
   * @param os JD
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code>
   */
  public static boolean nils(final Iterable<Object> os) {
    for (final Object ¢ : os)
      if (¢ == null)
        return true;
    return false;
  }

  /**
   * Determine whether a <code><b>null</b></code> occurs in a sequence of objects
   * 
   * @param os an unknown number of objects
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code>
   */
  @SafeVarargs public static <T> boolean nil(final @Nullable T o, final @Nullable T... os) {
    if (o == null || os == null)
      return true;
    for (final Object ¢ : os)
      if (¢ == null)
        return true;
    return false;
  }
}
