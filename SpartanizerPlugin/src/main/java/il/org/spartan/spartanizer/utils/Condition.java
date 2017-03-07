package il.org.spartan.spartanizer.utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-06 */
public interface Condition extends BooleanSupplier {
  static Inner and(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Conjunction(s1, s2, ss);
  }

  static Negation not(final BooleanSupplier ¢) {
    return new Negation(¢);
  }

  static Inner of(final BooleanSupplier ¢) {
    return new Wrapper(¢);
  }

  static Inner condition(final BooleanSupplier ¢) {
    return new Wrapper(¢);
  }

  static Inner or(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Disjunction(s1, s2, ss);
  }

  BooleanSupplier X = Condition.of(() -> {
    throw new AssertionError();
  });
  BooleanSupplier F = new Wrapper(() -> false);
  BooleanSupplier T = new Wrapper(() -> true);

  abstract class Compound implements Inner {
    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }

    final Inner add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      return this;
    }

    final Inner add(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.add(c);
      return add(cs);
    }

    final Inner add(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier... cs) {
      inner.add(c1);
      return add(c2, cs);
    }

    private final List<BooleanSupplier> inner = new ArrayList<>();
  }

  class Conjunction extends Compound {
    public Conjunction(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    Conjunction(final BooleanSupplier c, final BooleanSupplier[] cs) {
      add(c, cs);
    }

    @Override public Inner and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(this, c, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().allMatch(λ -> λ.getAsBoolean());
    }

    @Override public BooleanSupplier or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Disjunction(this, c, cs);
    }
  }

  final class Disjunction extends Compound {
    public Disjunction(final Conjunction c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    public Disjunction(final BooleanSupplier c, final BooleanSupplier... cs) {
      add(c, cs);
    }

    public Disjunction(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier[] cs) {
      add(c1, c2, cs);
    }

    @Override public Inner and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(c, cs);
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(λ -> λ.getAsBoolean());
    }

    @Override public BooleanSupplier or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(c, cs);
    }
  }

  interface Inner extends Condition {
    Inner and(BooleanSupplier c, BooleanSupplier... cs);

    BooleanSupplier or(BooleanSupplier c, BooleanSupplier... cs);
  }

  class Negation extends Wrapper {
    public Negation(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  class Wrapper implements Inner {
    public Wrapper(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    @Override public final Inner and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Conjunction(this, c, cs);
    }

    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }

    @Override public Inner or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Disjunction(this, c, cs);
    }

    public final BooleanSupplier inner;
  }
}
