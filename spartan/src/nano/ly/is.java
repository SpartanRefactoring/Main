/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package nano.ly;

import java.util.stream.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-23 */
public interface is {
  /** Determine if an item can be found in a list of values
   * @param < T > JD
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is found in the list */
  @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
    return Stream.of(ts).anyMatch(λ -> λ != null && λ.equals(candidate));
  }

  /** Determine if an integer can be found in a list of values
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list */
  @SafeVarargs @Contract(pure = true) static boolean intIsIn(final int candidate, @NotNull final int... is) {
    for (final int ¢ : is)
      if (¢ == candidate)
        return true;
    return false;
  }

  interface not {
    /** the candidate is not in ts */
    @SafeVarargs static <T> boolean in(final T candidate, final T... ts) {
      return !is.in(candidate, ts);
    }
  }
}
