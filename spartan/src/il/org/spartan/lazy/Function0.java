/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

/** A {@link FunctionalInterface} encapsulating a function with no arguments
 * @param <T> type of the function result
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
@FunctionalInterface
public interface Function0<T> {
  /** @return result of function application
   * @throws Exception in case the function failed */
  T ¢() throws Exception;
}