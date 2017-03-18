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
public interface B00L2 extends BooleanSupplier, Recursive<B00L2> {
  static B00L2 AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new AND(s1, s2, ss);
  }

  static B00L2 condition(final BooleanSupplier ¢) {
    return new P(¢);
  }

  static NOT NOT(final BooleanSupplier ¢) {
    return new NOT(¢);
  }

  static B00L2 OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new OR(s1, s2, ss);
  }

  static B00L2 S(final BooleanSupplier ¢) {
    return new P(¢);
  }

  B00L2 F = new P(() -> false);
  B00L2 T = new P(() -> true);
  B00L2 X = B00L2.S(() -> {
    throw new AssertionError();
  });

  B00L2 and(BooleanSupplier c, BooleanSupplier... cs);

  default boolean eval() {
    return getAsBoolean();
  }

  B00L2 or(BooleanSupplier c, BooleanSupplier... cs);

  abstract class Compound implements B00L2 {
    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }

    final B00L2 add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      return this;
    }

    final B00L2 add(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.add(c);
      return add(cs);
    }

    final B00L2 add(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier... cs) {
      inner.add(c1);
      return add(c2, cs);
    }

    final List<BooleanSupplier> inner = new ArrayList<>();
  }

  /** A {@link B00L2} that is conjunction
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class AND extends Compound {
    public AND(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    AND(final BooleanSupplier c, final BooleanSupplier[] cs) {
      add(c, cs);
    }

    @Override public B00L2 and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(this, c, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L2 or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new OR(this, c, cs);
    }
  }

  /** A {@link B00L2} that is disjunction
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  final class OR extends Compound {
    public OR(final BooleanSupplier c, final BooleanSupplier... cs) {
      add(c, cs);
    }

    public OR(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    @Override public B00L2 and(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.set(inner.size() - 1, AND(S(last(inner)), c, cs));
      return this;
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L2 or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(c, cs);
    }
  }

  /** A negated {@link B00L2}
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class NOT extends P {
    public NOT(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  /** A parenthesized {@link B00L2}
   * @author Yossi Gil <tt>Yossi.Gil@GMail.COM</tt>
   * @since 2017-03-19 */
  class P implements B00L2 {
    public P(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    @Override public final B00L2 and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new AND(this, c, cs);
    }

    @Override public B00L2 or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new OR(this, c, cs);
    }

    public final BooleanSupplier inner;

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }
  }
}
