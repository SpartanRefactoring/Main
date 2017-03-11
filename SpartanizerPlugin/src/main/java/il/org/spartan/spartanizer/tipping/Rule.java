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
   * instance. Should be overridden
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  boolean check(N n);

  /** Should be overridden */
  default String[] akas() {
    return new String[] { technicalName() };
  }

  /** Should be overridden */
  default String describe(final N ¢) {
    return trivia.gist(¢);
  }

  /** Should be overridden */
  default String description() {
    return technicalName();
  }

  /** Should be overridden */
  default String description(final N ¢) {
    return String.format(verb(), describe(¢));
  }

  /** Should be overridden */
  default Example[] examples() {
    return new Example[0];
  }

  /** Should not be overridden */
  default String technicalName() {
    return getClass().getSimpleName();
  }

  /** A wrapper function without ExclusionManager.
   * @param ¢ The ASTNode object on which we deduce the tip.
   * @return a tip given for the ASTNode ¢. */
  T tip(N ¢);

  /** Should be overridden */
  default String verb() {
    return String.format("apply '%s' to '%%s'", technicalName());
  }

  abstract class Stateful<R, T> implements Rule<R, T> {
    private R current;

    
    @Override public final boolean check(R n) {
      return ok(n); 
    }

    abstract boolean ok(R n);

    public R current() {
      return current;
    }


  }

  abstract class Delegator<N, T> extends Stateful<N, T> {
    public final Rule<N, T> inner;

    @Override public boolean ok(final N ¢) {
      return inner.check(¢);
    }

    @Override public String[] akas() {
      return inner.akas();
    }

    @Override public String describe(final N ¢) {
      return inner.describe(¢);
    }

    @Override public String description() {
      return inner.description();
    }

    @Override public String description(final N ¢) {
      return inner.description(¢);
    }

    @Override public Example[] examples() {
      return inner.examples();
    }

    @Override public String technicalName() {
      return inner.technicalName();
    }

    @Override public T tip(final N ¢) {
      return inner.tip(¢);
    }

    @Override public String verb() {
      return inner.verb();
    }

    public Delegator(final Rule<N, T> inner) {
      this.inner = inner;
    }
  }
}