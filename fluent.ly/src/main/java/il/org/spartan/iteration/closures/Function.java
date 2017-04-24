package il.org.spartan.iteration.closures;

import org.jetbrains.annotations.*;

/** An interface representing a typed function with a single argument.
 * @param <Argument> Type of argument
 * @param <Result> Type of result
 * @author Yossi Gil
 * @since 12/07/2007 */
public interface Function<Argument, Result> {
  /** Evaluate the function for the given input
   * @param f Input argument
   * @return Value of the function for the given argument */
  @NotNull Result eval(Argument f);
}
