/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import org.eclipse.jdt.annotation.*;

/** A {@link FunctionalInterface} encapsulating a function with three arguments
 * @param <T> type of the function result
 * @param <A1> type of the 1st argument
 * @param <A2> type of the 2nd argument
 * @param <A3> type of the 3rd argument
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
@FunctionalInterface
public interface Function3<T, A1, A2, A3> {
  /** Encapsulated function
   * @param ¢1 1st argument
   * @param ¢2 2nd argument
   * @param ¢3 3rd argument
   * @throws Exception in case the function failed
   * @return result of applying the function to the arguments */
   T ϑ(A1 ¢1, A2 ¢2, A3 ¢3) throws Exception;
}