package il.org.spartan.spartanizer.tipping;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;

/** An abstract interface defining tippers, bloaters, and light weight pattern
 * search, logging, computing stats, etc.
 * @param <N>
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public interface Rule<N, T> {
  /** Determine whether the parameter is "eligible" for application of this
   * instance.
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  boolean check(N n);

  default String[] akas() {
    return new String[] { technicalName() };
  }

  default String describe(final N ¢) {
    return trivia.gist(¢);
  }

  default String description() {
    return technicalName();
  }

  default String description(final N ¢) {
    return String.format(verb(), describe(¢));
  }

  default Example[] examples() {
    return new Example[0];
  }

  default String technicalName() {
    return getClass().getSimpleName();
  }

  /** A wrapper function without ExclusionManager.
   * @param ¢ The ASTNode object on which we deduce the tip.
   * @return a tip given for the ASTNode ¢. */
  T tip(N ¢);

  default String verb() {
    return String.format("apply '%s' to '%%s'", technicalName());
  }
}