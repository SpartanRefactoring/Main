package il.org.spartan.utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import fluent.ly.as;
import il.org.spartan.*;

/*** Third lecture:
 * <ol>
 * <li>discuss asString()
 * <li>discuss fullEval() *
 * </ol>
 * @author Yossi Gil
 * @since 2017-03-06 */
public interface Proposition extends BooleanSupplier {
  static Proposition AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return AND(null, s1, s2, ss);
  }

  static Proposition AND(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new And(toString, s1, s2, ss);
  }

  static Proposition not(final BooleanSupplier ¢) {
    return new Not(¢);
  }

  static Proposition that(final BooleanSupplier ¢) {
    return new Singleton(¢);
  }

  static Proposition that(final String toString, final BooleanSupplier s) {
    return new Singleton(toString, s);
  }

  static Proposition OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(s1, s2, ss);
  }

  static Proposition OR(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(toString, s1, s2, ss);
  }

  /** a {@link Proposition} which is {@code false} */
  Proposition F = new Singleton("F", () -> false);
  /** a {@link Proposition} whose evaluation fails with
   * {@link NullPointerException} */
  Proposition N = Proposition.that("N", () -> {
    throw new NullPointerException();
  });
  /** a {@link Proposition} which is {@code true} */
  Proposition T = Proposition.that("T", () -> true);
  /** a {@link Proposition} whose evaluation fails with
   * {@link AssertionError} */
  Proposition X = Proposition.that("X", () -> {
    throw new AssertionError();
  });

  /** Name must be distinct from but similar to
   * {@link #AND(BooleanSupplier, BooleanSupplier, BooleanSupplier...)} */
  Proposition and(BooleanSupplier s, BooleanSupplier... ss);

  default Proposition and(final String toString, final BooleanSupplier s) {
    return and(Proposition.that(toString, s));
  }

  default boolean eval() {
    return getAsBoolean();
  }

  Proposition or(BooleanSupplier s, BooleanSupplier... ss);

  default Proposition or(final String toString, final BooleanSupplier s) {
    return or(Proposition.that(toString, s));
  }

  default <R> R reduce(final PropositionReducer<R> ¢) {
    return ¢.reduce(this);
  }

  abstract class Aggregate<Inner> extends Outer<Inner> implements Proposition {
    public Aggregate(final String toString, final Inner inner) {
      super(inner);
      this.toString = toString;
    }

    @Override public final String toString() {
      return toString != null ? toString : inner instanceof Aggregate ? inner + "" : super.toString();
    }

    public String toString;
  }

  interface Operator1 extends Function<BooleanSupplier, BooleanSupplier> {
    /** */
  }

  final class And extends Some {
    And(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] ss) {
      super(null);
      add(s1, s2, ss);
    }

    And(final BooleanSupplier s, final BooleanSupplier[] ss) {
      super(null);
      add(s, ss);
    }

    And(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] ss) {
      super(toString);
      add(s1, s2, ss);
    }

    @Override public Proposition and(final BooleanSupplier s, final BooleanSupplier... ss) {
      return new And(this, s, ss);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public Proposition or(final BooleanSupplier s, final BooleanSupplier... ss) {
      return new Or(this, s, ss);
    }
  }

  final class Not extends Singleton {
    public Not(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  final class Or extends Some {
    public Or(final BooleanSupplier s, final BooleanSupplier... cs) {
      super(null);
      add(s, cs);
    }

    public Or(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] ss) {
      super(null);
      add(s1, s2, ss);
    }

    public Or(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] ss) {
      super(toString);
      add(s1, s2, ss);
    }

    @Override public Proposition and(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new And(this, s, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public Proposition or(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new Or(this, s, cs);
    }
  }

  /** A parenthesized {@link Proposition}
   * @author Yossi Gil
   * @since 2017-03-19 */
  class Singleton extends Aggregate<BooleanSupplier> implements Recursive.Atomic<Proposition> {
    public Singleton(final BooleanSupplier inner) {
      this(null, inner);
    }

    public Singleton(final String toString, final BooleanSupplier inner) {
      super(toString, inner);
    }

    @Override public final Proposition and(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new And(this, s, cs);
    }

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }

    @Override public Proposition or(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new Or(this, s, cs);
    }

    @Override public Proposition self() {
      return null;
    }
  }

  /** A compound {@link Proposition}
   * @author Yossi Gil
   * @since 2017-03-19 */
  abstract class Some extends Aggregate<List<BooleanSupplier>> {
    public Some(final String toString) {
      super(toString, an.empty.list());
    }

    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }

    protected void simplify() {
      final List<BooleanSupplier> newInner = stream().map(λ -> {
        if (!getClass().isInstance(λ))
          return Stream.of(λ);
        if (((Some) λ).toString != null)
          this.toString += ", " + λ;
        return ((Some) λ).inner.stream();
      }).flatMap(λ -> λ).collect(Collectors.toList());
      inner.clear();
      inner.addAll(newInner);
    }

    final Proposition add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      simplify();
      return this;
    }

    final Proposition add(final BooleanSupplier s, final BooleanSupplier... cs) {
      inner.add(s);
      return add(cs);
    }

    final Proposition add(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... cs) {
      inner.add(s1);
      return add(s2, cs);
    }
  }
}
