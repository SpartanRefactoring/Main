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
  static B00L AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new AND(s1, s2, ss);
  }

  static B00L condition(final BooleanSupplier ¢) {
    return new P(¢);
  }

  static NOT NOT(final BooleanSupplier ¢) {
    return new NOT(¢);
  }

  static B00L OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new OR(s1, s2, ss);
  }

  static B00L S(final BooleanSupplier ¢) {
    return new P(¢);
  }

  /** a {@link B00L} which is {@code false} */
  B00L F = new P(() -> false);
  /** a {@link B00L} which is {@code true} */
  B00L T = new P(() -> true);
  /** a {@link B00L} whose evaluation fails with {@link AssertionError} */
  B00L X = B00L.S(() -> {
    throw new AssertionError();
  });

  B00L and(BooleanSupplier c, BooleanSupplier... cs);

  default boolean eval() {
    return getAsBoolean();
  }

  B00L or(BooleanSupplier c, BooleanSupplier... cs);

  /** A compound {@link B00L}
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-19 */
  abstract class C implements B00L {
    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }

    final B00L add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      return this;
    }

    final B00L add(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.add(c);
      return add(cs);
    }

    final B00L add(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier... cs) {
      inner.add(c1);
      return add(c2, cs);
    }

    final List<BooleanSupplier> inner = new ArrayList<>();
  }

  class AND extends C {
    public AND(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    AND(final BooleanSupplier c, final BooleanSupplier[] cs) {
      add(c, cs);
    }

    @Override public B00L and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(this, c, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new OR(this, c, cs);
    }
  }

  final class OR extends C {
    public OR(final BooleanSupplier c, final BooleanSupplier... cs) {
      add(c, cs);
    }

    public OR(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    @Override public B00L and(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.set(inner.size() - 1, AND(S(last(inner)), c, cs));
      return this;
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(c, cs);
    }
  }

  class NOT extends P {
    public NOT(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  /** A parenthesized {@link B00L}
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class P implements B00L, Recursive.Atomic<B00L> {
    public P(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    @Override public final B00L and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new AND(this, c, cs);
    }

    @Override public B00L or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new OR(this, c, cs);
    }

    public final BooleanSupplier inner;

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }
  }
}
