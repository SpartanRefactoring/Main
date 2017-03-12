package il.org.spartan.spartanizer.tipping;

import static java.lang.String.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** An abstract interface defining tippers, bloaters, and light weight pattern
 * search, logging, computing stats, etc.
 * @param <T> type of elements for which the rule is applicable
 * @param <R> type of result of applying this rule
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public interface Rule<T, R> extends Function<T, R>, Multiplexor<Rule<T, R>> {
  /** Should be overridden */
  default String[] akas() {
    return new String[] { technicalName() };
  }

  /** Apply this instance to a parameter
   * @param ¢ subject of this application
   * @return result of application of this instance on the given subject */
  @Override R apply(T ¢);

  /** Determine whether the parameter is "eligible" for application of this
   * instance. Should be overridden
   * @param n JD
   * @return <code><b>true</b></code> <i>iff</i> the argument is eligible for
   *         the simplification offered by this object. */
  boolean check(T n);

  /** Should be overridden */
  default String describe(final T ¢) {
    return trivia.gist(¢);
  }

  /** Should be overridden */
  default String description() {
    return technicalName();
  }

  /** Should be overridden */
  default String description(final T ¢) {
    return format(verb(), describe(¢));
  }

  /** Should be overridden */
  default Example[] examples() {
    return new Example[0];
  }

  /** Should not be overridden */
  @Override default Stream<Rule<T, R>> stream() {
    return Stream.of(this);
  }

  /** Should not be overridden */
  default String technicalName() {
    return getClass().getSimpleName();
  }

  /** Should be overridden */
  default String verb() {
    return format("apply '%s' to '%%s'", technicalName());
  }

  abstract class CountingDelegator<T, R> extends Delegator<T, R> {
    Map<String, Integer> count = new LinkedHashMap<>();

    public CountingDelegator(final Rule<T, R> inner) {
      super(inner);
    }

    @Override public Void before(final String key, final Object... arguments) {
      count.putIfAbsent(key, Integer.valueOf(0));
      count.put(key, Box.it(count.get(key).intValue() + 1));
      return super.before(key, arguments);
    }
  }

  @SuppressWarnings("static-method")
  abstract class Delegator<T, R> extends Stateful<T, R> implements Listener<T, R> {
    public final Rule<T, R> inner;

    public Delegator(final Rule<T, R> inner) {
      this.inner = inner;
    }

    @Override public final String[] akas() {
      before("akas");
      return listenAkas(inner::akas);
    }

    @Override public final R apply(final T ¢) {
      before("apply");
      return listenTip(inner::apply, ¢);
    }

    public Void before(@SuppressWarnings("unused") final String key, @SuppressWarnings("unused") final Object... arguments) {
      return null;
    }

    @Override public final String describe(final T n) {
      return listenDescribe(inner::describe, n);
    }

    @Override public final String description() {
      return listenDescription(inner::description);
    }

    @Override public final String description(final T ¢) {
      return listenDescription(() -> inner.description(¢));
    }

    @Override public final Example[] examples() {
      return listenExamples(inner::examples);
    }

    @Override public boolean ok(final T ¢) {
      before("ok");
      return listenOk(() -> inner.check(¢));
    }

    @Override public final String technicalName() {
      return listenTechnicalName(inner::technicalName);
    }

    @Override public final String verb() {
      before("verb");
      return listenVerb(inner::verb);
    }
  }

  interface Listener<T, R> extends Supplier<T> {
    @Override T get();

    default String[] listenAkas(final Supplier<String[]> $) {
      return $.get();
    }

    /** [[SuppressWarningsSpartan]] */
    default String listenDescribe(final Function<T, String> f, final T t) {
      return f.apply(t);
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

    default R listenTip(final Function<T, R> f, final T t) {
      return f.apply(t);
    }

    default String listenVerb(final Supplier<String> $) {
      return $.get();
    }
  }

  abstract class Stateful<T, R> implements Rule<T, R>, Supplier<T> {
    private T get;

    @Override public R apply(final T ¢) {
      if (get() == null)
        return badTypeState(//
            "Attempt to apply rule before previously checking\n" + //
                "    Argument to rule application is: %s\n",
            ¢);
      if (¢ != get())
        return badTypeState(//
            "Argument to rule application is distinct from previous checked argument\n" + //
                "    Previously checked arguments was: %s\n" + //
                "    Argument to rule application is: %s\n",
            ¢, get());
      final R $ = fire();
      get = null;
      return $;
    }

    private R badTypeState(final String reason, final Object... os) {
      return monitor.logProbableBug(this,
          new IllegalStateException(//
              format(//
                  "Invalid order of method calls on a %s (dynamic type %):\n", //
                  system.className(Rule.class), //
                  system.className(this)) //
                  + //
                  format(//
                      "  REASON: %s\n", //
                      format(reason, os)//
                  )//
          )//
      );
    }

    @Override public final boolean check(final T n) {
      get = n;
      return ok(n);
    }

    @Override public final T get() {
      return get;
    }

    public abstract R fire();

    public abstract boolean ok(T n);
  }
}
