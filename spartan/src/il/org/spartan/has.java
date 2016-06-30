/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

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
   * Determine whether a <code><b>null</b></code> occurs in a sequence of
   * objects
   *
   * @param os JD
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code>
   */
  public static boolean nulls(final Object... os) {
    for (final Object o : os)
      if (o == null)
        return true;
    return false;
  }
  /**
   * Determine whether a <code><b>null</b></code> occurs in a sequence of
   * objects
   *
   * @param os JD
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code>
   */
  public static boolean nulls(final Iterable<@Nullable Object> os) {
    for (final Object o : os)
      if (o == null)
        return true;
    return false;
  }
}
