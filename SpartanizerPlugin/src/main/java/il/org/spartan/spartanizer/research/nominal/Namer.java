package il.org.spartan.spartanizer.research.nominal;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** Explore ways to give meaningful names to identifiers according using
 * different strategies.
 * @author Dor Ma'ayan
 * @since 2017-05-20 */
public class Namer {
  /** The semantics of function {@link #apply(Type)} here is this:
   * <ol>
   * <li>If the argument is {@code null} then the result is {@code null}</li>
   * <li>If the argument is not {@code null} then the result is {@code null} if
   * the argument <em>can not<em/> be simplified by this strategy</li>
   * <li>A non-{@code null} is returned, then the this instance can offer a
   * simplification of this the result <em>can not<em/> be simplified by this
   * strategy</li>
   * </ol>
   * @author Yossi Gil
   * @since 2017-05-20 */
  @FunctionalInterface
  public interface Simplifier extends Function<Type, Simplification> {
    // TODO Dor Maayan
  }

  /** This is the actual simplification.
   * @author Yossi Gil
   * @since 2017-05-20 */
  public interface Simplification extends UnaryOperator<String>, Supplier<Type>{
    /** This is how to change the result of further simplifications */
    @Override default String apply(final String ¢) {
      return ¢;
    }
    /** This is the result of this simplification, guaranteed to be
     * non-{@code null} and having fewer nodes than the input. */
    @Override default Type get() {
      return null;
    }
  }
  
  public interface AtomicSimplification extends Simplification, Duplo.Atomic<Simplification>{
    //Empty
  }
  
  public interface CompundSimplification extends Simplification, Duplo.Compound<Simplification>{
    //Empty
  }

  static final String PLURALS = "s";
  // An example of simplification
  public static Simplifier arrayAtomic = λ -> new AtomicSimplification() {
    @Override public String apply(final String ¢) {
      return ¢ + PLURALS;
    }
    @Override public Type get() {
      return !λ.isArrayType() ? null : az.arrayType(λ).getElementType();
    }
    @Override public Simplification self() {
      return this;
    }
  };
  
  public static Simplifier arrayCompund = λ -> new CompundSimplification() {
    @Override public String apply(final String ¢) {
      return ¢ + PLURALS;
    }
    @Override public Type get() {
      return !λ.isArrayType() ? null : az.arrayType(λ).getElementType();
    }
    @Override public Simplification self() {
      return this;
    }
    @Override public Iterable<? extends Duplo<Simplification>> neighbors() {
      return null;
    }
  };
}
