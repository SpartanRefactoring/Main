package il.org.spartan.iteration.closures;

/** An interface representing a typed procedure with a single argument.
 * @param <Argument> Type of argument */
public interface Procedure<Argument> {
  /** Execute the procedure with the given input
   * @param a Input argument */
  void eval(Argument a);
}
