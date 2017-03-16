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
    return new Conjunction(s1, s2, ss);
  }

  static B00L condition(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  static Negation NOT(final BooleanSupplier ¢) {
    return new Negation(¢);
  }

  static B00L OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Disjunction(s1, s2, ss);
  }

  static B00L S(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  B00L F = new Parenthesis(() -> false);
  B00L T = new Parenthesis(() -> true);
  B00L X = B00L.S(() -> {
    throw new AssertionError();
  });

  B00L and(BooleanSupplier c, BooleanSupplier... cs);

  default boolean eval() {
    return getAsBoolean();
  }

  B00L or(BooleanSupplier c, BooleanSupplier... cs);

  abstract class Compound implements B00L {
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

  class Conjunction extends Compound {
    public Conjunction(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    Conjunction(final BooleanSupplier c, final BooleanSupplier[] cs) {
      add(c, cs);
    }

    @Override public B00L and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(this, c, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override public B00L or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Disjunction(this, c, cs);
    }
  }

  final class Disjunction extends Compound {
    public Disjunction(final BooleanSupplier c, final BooleanSupplier... cs) {
      add(c, cs);
    }

    public Disjunction(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
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

  class Negation extends Parenthesis {
    public Negation(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  class Parenthesis implements B00L {
    public Parenthesis(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    @Override public final B00L and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Conjunction(this, c, cs);
    }

    @Override public B00L or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Disjunction(this, c, cs);
    }

    public final BooleanSupplier inner;

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }
  }
}
