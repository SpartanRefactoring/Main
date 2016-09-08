/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

/** A collection of <code><b>static</b></code> functions for converting from one
 * aggregate type to another.
 * @author Yossi Gil
 * @since Jul 8, 2014 */
public enum has {
  ;
  @SuppressWarnings("javadoc") public static class TEST {
    private final @Nullable String nul = null;

    @Test public void seriesA01() {
      azzert.aye(has.nulls(nul));
    }

    @SuppressWarnings("static-method") @Test public void seriesA02() {
      azzert.nay(has.nulls("A"));
    }
  }

  /** Retrieve next item in a list
   * @param <T> JD
   * @param i an index of specific item in a list
   * @param ts the indexed list
   * @return following item in the list, if such such an item exists, otherwise,
   *         the last node */
  public static <@Nullable T> @Nullable T next(final int i, final List<T> ts) {
    return !inRange(i + 1, ts) ? last(ts) : ts.get(i + 1);
  }

  /** Determine whether a <code><b>null</b></code> occurs in a sequence of
   * objects
   * @param os JD
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code> */
  public static boolean nulls(final Iterable<@Nullable Object> os) {
    for (final Object o : os)
      if (o == null)
        return true;
    return false;
  }

  /** Determine whether a <code><b>null</b></code> occurs in a sequence of
   * objects
   * @param os JD
   * @return <code><b>null</b></code> <i>iff</i> one of the parameters is
   *         <code><b>null</b></code> */
  public static boolean nulls(final Object... os) {
    for (final Object o : os)
      if (o == null)
        return true;
    return false;
  }
}
