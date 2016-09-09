package il.org.spartan.iteration.closures;

/** An interface representing a procedure which takes two arguments
 * @param <A1> type of first argument
 * @param <A2> type of second argument */
public interface Procedure2<A1, A2> {
  /** Execute the procedure
   * @param a1 first argument
   * @param a2 second argument
   * @return nothing */
  public Void eval(A1 a1, A2 a2);
}
