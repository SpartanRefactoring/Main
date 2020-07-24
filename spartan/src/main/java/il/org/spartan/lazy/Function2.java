/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

/** A {@link FunctionalInterface} encapsulating a function with two arguments
 *
 * @param <T>  type of the function result
 * @param <A1> type of the 1st argument
 * @param <A2> type of the 2nd argument
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
@FunctionalInterface public interface Function2<T, A1, A2> {
  /** Encapsulated function
   *
   * @param ¢1 1st argument
   * @param ¢2 2nd argument
   * @throws Exception in case the function failed
   * @return result of applying the function to the arguments */
  T ϑ(A1 ¢1, A2 ¢2) throws Exception;
}