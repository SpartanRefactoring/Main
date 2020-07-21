/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

/** A {@link FunctionalInterface} encapsulating a function with four arguments
 * @param <T> type of the function result
 * @param <A1> type of the 1st argument
 * @param <A2> type of the 2nd argument
 * @param <A3> type of the 3rd argument
 * @param <A4> type of the 4th argument
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
@FunctionalInterface
public interface Function4<T, A1, A2, A3, A4> {
  /** Encapsulated function
   * @param ¢1 1st argument
   * @param ¢2 2nd argument
   * @param ¢3 3rd argument
   * @param ¢4 4th argument
   * @return result of applying the function to the arguments
   * @throws Exception in case the function failed */
  T ϑ(A1 ¢1, A2 ¢2, A3 ¢3, A4 ¢4) throws Exception;
}