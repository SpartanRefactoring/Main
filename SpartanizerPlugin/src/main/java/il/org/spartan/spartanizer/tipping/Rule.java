package il.org.spartan.spartanizer.tipping;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;
import il.org.spartan.utils.*;

/** An abstract interface defining tippers, bloaters, and light weight pattern
 * search, logging, computing stats, etc.
 * @param <N>
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public interface Rule<N, T> extends Multiplexor<Rule<N, T>> {
  /** Should be overridden */
  default String[] akas() {
    return new String[] { technicalName() };
  }

  /** Determine whether the parameter is "eligible" for application of this
   * instance. Should be overridden
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  boolean check(N n);

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
  @Override default Stream<Rule<N, T>> stream() {
    return Stream.of(this);
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

  abstract class CountingDelegator<R, T> extends Delegator<R, T> {
    public CountingDelegator(final Rule<R, T> inner) {
      super(inner);
    }

    @Override public Void before(final String key, final Object... arguments) {
      count.putIfAbsent(key, Integer.valueOf(0));
      count.put(key, Box.it(count.get(key).intValue() + 1));
      return super.before(key, arguments);
    }

    Map<String, Integer> count = new LinkedHashMap<>();
  }

  @SuppressWarnings("static-method")
  abstract class Delegator<N, T> extends Stateful<N, T> implements Listener<N, T> {
    public Delegator(final Rule<N, T> inner) {
      this.inner = inner;
    }

    @Override public final String[] akas() {
      before("akas");
      return listenAkas(() -> inner.akas());
    }

    public Void before(@SuppressWarnings("unused") final String key, @SuppressWarnings("unused") final Object... arguments) {
      return null;
    }

    @Override public final String describe(final N n) {
      return listenDescribe((final N x) -> inner.describe(x), n);
    }

    @Override public final String description() {
      return listenDescription(() -> inner.description());
    }

    @Override public final String description(final N ¢) {
      return listenDescription(() -> inner.description(¢));
    }

    @Override public final Example[] examples() {
      return listenExamples(() -> inner.examples());
    }

    @Override public boolean ok(final N ¢) {
      before("ok");
      return listenOk(() -> inner.check(¢));
    }

    @Override public final String technicalName() {
      return listenTechnicalName(() -> inner.technicalName());
    }

    @Override public final T tip(final N ¢) {
      before("tip");
      return listenTip(λ -> inner.tip(λ), ¢);
    }

    @Override public final String verb() {
      before("verb");
      return listenVerb(() -> inner.verb());
    }

    public final Rule<N, T> inner;
  }

  interface Listener<N, T> {
    default String[] listenAkas(final Supplier<String[]> $) {
      return $.get();
    }

    /** [[SuppressWarningsSpartan]] */
    default String listenDescribe(final Function<N, String> f, final N n) {
      return f.apply(n);
    }

    default String listenDescription(final Supplier<String> $) {
      return $.get();
    }

    default Example[] listenExamples(final Supplier<Example[]> $) {
      return $.get();
    }

    default boolean listenOk(final BooleanSupplier ¢) {
      return ¢.getAsBoolean();
    }

    default String listenTechnicalName(final Supplier<String> $) {
      return $.get();
    }

    default T listenTip(final Function<N, T> f, final N n) {
      return f.apply(n);
    }

    default String listenVerb(final Supplier<String> $) {
      return $.get();
    }
  }

  abstract class Stateful<R, T> implements Rule<R, T> {
    @Override public final boolean check(final R n) {
      return ok(n);
    }

    public R current() {
      return current;
    }

    abstract boolean ok(R n);

    private R current;
  }
}
