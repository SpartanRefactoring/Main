package il.org.spartan.misc;

/** An interface representing the factory concept. Every factory class must have
 * a method that given a string, i.e., a literal, returns an actual value.
 * @param <V> The super type of all run-time values
 * @author Yossi Gil
 * @since 21/05/2007 */
public interface FactoryConcept<V> {
  /** convert a literal into an actual value
   * @param s a string literal
   * @return the actual value */
  V fromString(String s);
}
