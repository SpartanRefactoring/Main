package il.org.spartan.utils;
import static il.org.spartan.lisp.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-06 */
public interface SymbolicPredicate extends BooleanSupplier {
  static Inner and(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Conjunction(s1, s2, ss);
  }

  static Negation NOT(final BooleanSupplier ¢) {
    return new Negation(¢);
  }

  static Inner of(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  static Inner condition(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  static Inner OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Disjunction(s1, s2, ss);
  }

  SymbolicPredicate X = SymbolicPredicate.of(() -> {
    throw new AssertionError();
  });
  SymbolicPredicate F = new Parenthesis(() -> false);
  SymbolicPredicate T = new Parenthesis(() -> true);

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

     final List<BooleanSupplier> inner = new ArrayList<>();
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

    @Override public Inner or(final BooleanSupplier c, final BooleanSupplier... cs) {
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

    @Override public Inner and(final BooleanSupplier c, final BooleanSupplier... cs) {
      final BooleanSupplier last = last(inner);
      SymbolicPredicate x = SymbolicPredicate.of(last);
      final Inner and = SymbolicPredicate.and(x,c,cs);
      inner.set(inner.size()-1, and); 
      return this;
    }

    @Override public boolean getAsBoolean() {
      return stream().anyMatch(λ -> λ.getAsBoolean());
    }

    @Override public Inner or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return add(c, cs);
    }
  }

  interface Inner extends SymbolicPredicate {
    Inner and(BooleanSupplier c, BooleanSupplier... cs);

    default boolean eval() {
      return getAsBoolean();
    }

    Inner or(BooleanSupplier c, BooleanSupplier... cs);
  }

  class Negation extends Parenthesis {
    public Negation(final BooleanSupplier s) {
      super(s);
    }

    @Override public boolean getAsBoolean() {
      return !inner.getAsBoolean();
    }
  }

  class Parenthesis implements Inner {
    public Parenthesis(final BooleanSupplier inner) {
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
