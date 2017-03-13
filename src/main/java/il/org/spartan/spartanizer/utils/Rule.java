package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static java.lang.String.*;

import java.lang.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.tipping.Tipper.*;
import il.org.spartan.utils.*;

/** An abstract interface defining tippers, bloaters, and light weight pattern
 * search, logging, computing statistics, etc.
 * <p>
 * A rule is a {@link Function} that is augmented with:
 * <ol>
 * <li><b>Typestate semantics:</b> Protocol is call to @{@link Check}
 * {@link #check(Object)}, and only then @{@link Apply} {@link #apply(Object)}.
 * <p>
 * The order of method application must follow match the regular expression
 * {@code (CC*A?)*}. This order is enforced by {@link Rule.Stateful} that, when
 * possible, should be extended by clients.
 * <p>
 * An instance can thus be in one of two states:
 * <ol>
 * <li>{@link #ready()}, which is the state after {@code @}{@link Check} method
 * call, and a prerequisite for @{@link Apply} method call.</li>
 * <li>not {@link #ready()}, which is the initial state, and the state after
 * a @{@link Apply} method call. .</li>
 * </ol>
 * <li><b>Aggregation:</b> An instance of this class may be atomic, or compound,
 * i.e., implementing RuleMulti. Method {@link #descendants()} returns a
 * {@link Stream} of nested instances, which is an singleton of {@code this} if
 * the instance is atomic, or include any number of instances, including
 * <li><b>Descriptive qualities:</b> {@link #examples()}, {@link #akas()},
 * {@link #describe(Object)}, {@link #description(Object)}, and more.
 * </ol>
 * <p>
 * Note: In sub-classes the sub-interfaces methods marked {@code @}{@link Check}
 * and {@code @}{@link Apply} are often overridden and marked {@code final},
 * while delegating to another (typically {@code abstract}) method. This other
 * methods should be marked as either {@code @}{@link Apply} or
 * {@code @}{@link Check}.
 * <p>
 * TODO:
 * <nl>
 * <li>List of descriptive qualities requires re-thinking.
 * <li>Convert {@code @}{@link Check} and {@code @}{@link Apply} into a single
 * annotation, that takes an {@code enum} defined to include all state .
 * <li>No method other than {@link #check(Object)} should be allowed to take
 * parameters. Defunct {@link #describe(Object)} and
 * {@link #description(Object)}.
 * </nl>
 * @param <T> type of elements for which the rule is applicable
 * @param <R> type of result of applying this rule
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-10 */
public interface Rule<@¢ T, @¢ R> extends Function<T, R>, Recursive<Rule<T, R>> {
  /** Should be overridden */
  default String[] akas() {
    return new String[] { technicalName() };
  }

  /** Apply this instance to a parameter
   * @param ¢ subject of this application
   * @return result of application of this instance on the given subject */
  @Override @Apply R apply(T ¢);

  /** Determine whether the parameter is "eligible" for application of this
   * instance. Should be overridden
   * @param n JD
   * @return whether the argument is eligible for the simplification offered by
   *         this instance. */
  @Check boolean check(T n);

  default String description() {
    return format("%s/[%s]%s=", //
        className(Rule.class), //
        className(this), //
        technicalName() == className(this) ? "" : technicalName(), //
        technicalName(), //
        !ready() ? "not ready to " : "ready to " + verbObject());
  }

  /** Should be overridden */
  default Example[] examples() {
    return new Example[0];
  }

  default T object() {
    return null;
  }

  default boolean ready() {
    return object() != null;
  }

  /** Should not be overridden */
  default String technicalName() {
    return getClass().getSimpleName();
  }

  /** Should be overridden */
  default String verb() {
    return format("apply '%s' to '%%s'", technicalName());
  }

  default String verbObject() {
    return format(verb(), object());
  }

  @Documented
  @Inherited
  @Target(ElementType.METHOD)
  @interface Apply {
    String value() default "A method for applying this instance";
  }

  @Documented
  @Inherited
  @Target(ElementType.METHOD)
  @interface Check {
    String value() default "A boolean method for checking prior to application of this instance";
  }

  abstract class CountingDelegator<T, R> extends Delegator<T, R> {
    final Map<String, Integer> count = new LinkedHashMap<>();

    public CountingDelegator(final Rule<T, R> inner) {
      super(inner);
    }

    @Override public Void before(final String key, final Object... arguments) {
      count.putIfAbsent(key, Integer.valueOf(0));
      count.put(key, Box.it(count.get(key).intValue() + 1));
      return super.before(key, arguments);
    }
  }

  /** Default implementation of a delegating {@link Rule}, equipped with a
   * listener.
   * @param <T>
   * @param <R>
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
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

    @Override @Apply public final R apply(final T ¢) {
      before("apply");
      return listenTip(inner::apply, ¢);
    }

    public Void before(@SuppressWarnings("unused") final String key, @SuppressWarnings("unused") final Object... arguments) {
      return null;
    }

    @Override public final String description() {
      return listenDescription(inner::description);
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

  /** Default implementation of {@link Rule},
   * @param <T> {@see Rule}
   * @param <R> {@see Rule}
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
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
                "    Operand to rule application is: %s\n",
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

    public abstract R fire();

    @Override public final T get() {
      return get;
    }

    public abstract boolean ok(T n);
  }
}
