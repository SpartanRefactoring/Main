package il.org.spartan.utils;


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
public interface B00L extends BooleanSupplier {
  /** a {@link B00L} which is {@code false} */
  B00L F = new P("F", () -> false);
  /** a {@link B00L} whose evaluation fails with {@link NullPointerException} */
  B00L N = B00L.of("N", () -> {
    throw new NullPointerException();
  });
  /** a {@link B00L} which is {@code true} */
  B00L T = new P("T", () -> true);
  /** a {@link B00L} whose evaluation fails with {@link AssertionError} */
  B00L X = B00L.of("X", () -> {
    throw new AssertionError();
  });

  static B00L AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new And(s1, s2, ss);
  }

  static B00L AND(final String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new And(toString, s1, s2, ss);
  }


  static Not NOT(final BooleanSupplier ¢) {
    return new Not(¢);
  }

  static B00L of(final BooleanSupplier ¢) {
    return new P(¢);
  }

  static B00L of(final String toString, final BooleanSupplier s) {
    return new P(toString, s);
  }

  static B00L OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(s1, s2, ss);
  }
  static B00L OR(String toString, final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Or(toString, s1, s2, ss);
  }


  /** Name must be distinct from but similar to
   * {@link #AND(BooleanSupplier, BooleanSupplier, BooleanSupplier...)} */
  B00L and(BooleanSupplier s, BooleanSupplier... ss);

  default boolean eval() {
    return getAsBoolean();
  }

  B00L or(BooleanSupplier s, BooleanSupplier... ss);

  default <R> R reduce(final B00LReducingGear<R> ¢) {
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

    And(String toString, BooleanSupplier s1, BooleanSupplier s2, BooleanSupplier[] ss) {
      super(toString);
      add(s1, s2, ss);
    }

    @Override public B00L and(final BooleanSupplier s, final BooleanSupplier... ss) {
      return add(this, s, ss);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L or(final BooleanSupplier s, final BooleanSupplier... ss) {
      return new Or(this, s, ss);
    }
  }

  /** A compound {@link B00L}
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-19 */
  abstract class C implements B00L {
    final List<BooleanSupplier> inner = new ArrayList<>();
    private final String toString;

    @Override public String toString() {
      return toString != null ? toString : super.toString();
    }
    public C(String toString) {
     this.toString = toString; 
    }

    final B00L add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      return this;
    }

    final B00L add(final BooleanSupplier s, final BooleanSupplier... cs) {
      inner.add(s);
      return add(cs);
    }

    final B00L add(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... cs) {
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

    public Or(String toString, BooleanSupplier s1, BooleanSupplier s2, BooleanSupplier[] ss) {
      super(toString);
      add(s1, s2, ss);
    }

    @Override public B00L and(final BooleanSupplier s, final BooleanSupplier... cs) {
      inner.set(inner.size() - 1, AND(of(last(inner)), s, cs));
      return this;
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L or(final BooleanSupplier s, final BooleanSupplier... cs) {
      return add(s, cs);
    }
  }

  /** A parenthesized {@link B00L}
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class P implements B00L, Recursive.Atomic<B00L> {
    public final BooleanSupplier inner;

    public P(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    public P(String toString, final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = new BooleanSupplier() {
        @Override public boolean getAsBoolean() {
          return inner.getAsBoolean();
        }

        @Override public String toString() {
          return toString;
        }
      };
    }

    @Override public final B00L and(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new And(this, s, cs);
    }

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }

    @Override public B00L or(final BooleanSupplier s, final BooleanSupplier... cs) {
      return new Or(this, s, cs);
    }
  }

}
