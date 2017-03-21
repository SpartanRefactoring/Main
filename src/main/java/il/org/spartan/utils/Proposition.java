package il.org.spartan.utils;

import static il.org.spartan.utils.Proposition.*;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.*;

/*** <p>
 * TODO second lecture
 * <ol>
 * <li>discuss asString()
 * <li>discuss fullEval() *
 * </ol>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-06 */
public interface Proposition extends BooleanSupplier {
  /** a {@link Proposition} which is {@code false} */
  Proposition F = new P("F", () -> false);
  /** a {@link Proposition} whose evaluation fails with
   * {@link NullPointerException} */
  Proposition N = Proposition.of("N", () -> {
    throw new NullPointerException();
  });
  /** a {@link Proposition} which is {@code true} */
  Proposition T = new P("T", () -> true);
  /** a {@link Proposition} whose evaluation fails with
   * {@link AssertionError} */
  Proposition X = Proposition.of("X", () -> {
    throw new AssertionError();
  });

  static Proposition AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return AND(null, s1, s2, ss);
  }

  static Proposition AND(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new And(toString, s1, s2, ss);
  }

  static Proposition NOT(final BooleanSupplier ¢) {
    return new Not(¢);
  }

  static Proposition of(final BooleanSupplier ¢) {
    return new P(¢);
  }

  static Proposition of(final String toString, final BooleanSupplier s) {
    return new P(toString, s);
  }

  static Proposition OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(s1, s2, ss);
  }

  static Proposition OR(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(toString, s1, s2, ss);
  }

  /** Name must be distinct from but similar to
   * {@link #AND(BooleanSupplier, BooleanSupplier, BooleanSupplier...)} */
  Proposition and(BooleanSupplier s, BooleanSupplier... ss);

  default boolean eval() {
    return getAsBoolean();
  }

  Proposition or(BooleanSupplier s, BooleanSupplier... ss);

  default <R> R reduce(final PropositionReducer<R> ¢) {
    return ¢.reduce(this);
  }

  class And extends C {
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
      return add(this, s, ss);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public Proposition or(final BooleanSupplier s, final BooleanSupplier... ss) {
      return new Or(this, s, ss);
    }
  }

  abstract class Implementation<Inner> extends Outer<Inner> implements Proposition {
    protected final String toString;

    public Implementation(final String toString, final Inner inner) {
      super(inner);
      this.toString = toString;
    }

    @Override public String toString() {
      return inner instanceof Implementation ? inner + "" : toString != null ? toString : super.toString();
    }
  }

  /** A compound {@link Proposition}
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-19 */
  abstract class C extends Implementation<List<BooleanSupplier>> {
    public C(final String toString) {
      super(toString, new ArrayList<>());
    }

    final Proposition add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
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

    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }
  }

  final class Not extends P {
    public Not(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  final class Or extends C {
    public Or(final BooleanSupplier s, final BooleanSupplier... cs) {
      super(null);
      add(s, cs);
    }

    public Or(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] cs) {
      super(null);
      add(s1, s2, cs);
    }

    public Or(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier[] ss) {
      super(toString);
      add(s1, s2, ss);
    }

    @Override public Proposition and(final BooleanSupplier s, final BooleanSupplier... cs) {
      inner.set(inner.size() - 1, AND(of(last(inner)), s, cs));
      return this;
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public Proposition or(final BooleanSupplier s, final BooleanSupplier... cs) {
      return add(s, cs);
    }
  }

  /** A parenthesized {@link Proposition}
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class P extends Implementation<BooleanSupplier> implements Recursive.Atomic<Proposition> {
    public P(final BooleanSupplier inner) {
      this(null, inner);
    }

    public P(final String toString, final BooleanSupplier inner) {
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
  }
}
