package il.org.spartan.utils;
import static il.org.spartan.lisp.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-06 */
public interface SymbolicPredicate extends BooleanSupplier {

  static SymbolicPredicate AND(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Conjunction(s1, s2, ss);
  }
  static SymbolicPredicate condition(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  static Negation NOT(final BooleanSupplier ¢) {
    return new Negation(¢);
  }

  static SymbolicPredicate OR(final BooleanSupplier s1, final BooleanSupplier s2, final BooleanSupplier... ss) {
    return new Disjunction(s1, s2, ss);
  }

  static SymbolicPredicate S(final BooleanSupplier ¢) {
    return new Parenthesis(¢);
  }

  SymbolicPredicate F = new Parenthesis(() -> false);

  SymbolicPredicate T = new Parenthesis(() -> true);
  SymbolicPredicate X = SymbolicPredicate.S(() -> {
    throw new AssertionError();
  });
  SymbolicPredicate and(BooleanSupplier c, BooleanSupplier... cs);

  default boolean eval() {
    return getAsBoolean(); 
  }

  SymbolicPredicate or(BooleanSupplier c, BooleanSupplier... cs);

   abstract class Compound implements SymbolicPredicate {
    protected Stream<BooleanSupplier> stream() {
      return inner.stream();
    }

    final SymbolicPredicate add(final BooleanSupplier... ¢) {
      inner.addAll(as.list(¢));
      return this;
    }

    final SymbolicPredicate add(final BooleanSupplier c, final BooleanSupplier... cs) {
      inner.add(c);
      return add(cs);
    }

    final SymbolicPredicate add(final BooleanSupplier c1, final BooleanSupplier c2, final BooleanSupplier... cs) {
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

      @Override public SymbolicPredicate and(final BooleanSupplier c, final BooleanSupplier... cs) {
        return add(this, c, cs);
      }

      @Override public boolean getAsBoolean() {
        return stream().allMatch(λ -> λ.getAsBoolean());
      }

      @Override public SymbolicPredicate or(final BooleanSupplier c, final BooleanSupplier... cs) {
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

      @Override public SymbolicPredicate and(final BooleanSupplier c, final BooleanSupplier... cs) {
        inner.set(inner.size()-1, AND(S(last(inner)), c, cs)); 
        return this;
      }

      @Override public boolean getAsBoolean() {
        return stream().anyMatch(λ -> λ.getAsBoolean());
      }

      @Override public SymbolicPredicate or(final BooleanSupplier c, final BooleanSupplier... cs) {
        return add(c, cs);
      }
    }

  class Negation extends Parenthesis {
    public Negation(final BooleanSupplier s) {
      super(s);
    }

  }

  class Parenthesis implements SymbolicPredicate {
    public Parenthesis(final BooleanSupplier inner) {
      if (inner == null)
        throw new IllegalArgumentException();
      this.inner = inner;
    }

    @Override public final SymbolicPredicate and(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Conjunction(this, c, cs);
    }


    @Override public SymbolicPredicate or(final BooleanSupplier c, final BooleanSupplier... cs) {
      return new Disjunction(this, c, cs);
    }

    public final BooleanSupplier inner;


    @Override public boolean getAsBoolean() {
      return inner.getAsBoolean();
    }
  }
}
