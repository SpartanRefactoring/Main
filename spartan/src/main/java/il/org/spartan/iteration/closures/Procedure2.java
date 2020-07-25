package il.org.spartan.iteration.closures;

/** An interface representing a procedure which takes two arguments
 *
 * @param <A1> type of first argument
 * @param <A2> type of second argument */
public interface Procedure2<A1, A2> {
  /** Execute the procedure
   *
   * @param a  first argument
   * @param a2 second argument
   * @return nothing */
  Void eval(A1 a, A2 a2);
}
