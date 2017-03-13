package il.org.spartan.utils;

import static il.org.spartan.utils.SymbolicPredicate.*;

import java.util.function.*;

import org.junit.*;

/** Tests class {@link SymbolicPredicate}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class SymbolicPredicateTest {
  Object object;
  BooleanSupplier supplier;
  SymbolicPredicate condition;
  SymbolicPredicate inner;

  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }

  @Test public void aa() {
    object = SymbolicPredicate.T;
    object = SymbolicPredicate.F;
    object = SymbolicPredicate.T;
    object = SymbolicPredicate.F;
    assert SymbolicPredicate.T != null;
    assert SymbolicPredicate.T != null;
    assert SymbolicPredicate.F != null;
    assert SymbolicPredicate.T != null;
    assert SymbolicPredicate.F != null;
    supplier = SymbolicPredicate.T;
    supplier = SymbolicPredicate.F;
    supplier = SymbolicPredicate.T;
    supplier = SymbolicPredicate.F;
    assert ignoreNext() || SymbolicPredicate.T.getAsBoolean();
    assert ignoreNext() || SymbolicPredicate.F.getAsBoolean();
    assert ignoreNext() || SymbolicPredicate.T.getAsBoolean();
    assert ignoreNext() || SymbolicPredicate.F.getAsBoolean();
    assert SymbolicPredicate.T.getAsBoolean();
    assert !SymbolicPredicate.F.getAsBoolean();
    assert SymbolicPredicate.T.getAsBoolean();
    assert !SymbolicPredicate.F.getAsBoolean();
    // of() exists
    SymbolicPredicate.S(SymbolicPredicate.T);
    SymbolicPredicate.S(SymbolicPredicate.F);
    SymbolicPredicate.S(SymbolicPredicate.T);
    SymbolicPredicate.S(SymbolicPredicate.F);
    // of() is not void
    (SymbolicPredicate.S(SymbolicPredicate.T) + "").hashCode();
    (SymbolicPredicate.S(SymbolicPredicate.F) + "").hashCode();
    (SymbolicPredicate.S(SymbolicPredicate.T) + "").hashCode();
    (SymbolicPredicate.S(SymbolicPredicate.F) + "").hashCode();
    // of() returns not null
    assert SymbolicPredicate.S(SymbolicPredicate.T) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.F) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.T) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.F) != null;
    // of() returns an object
    object = SymbolicPredicate.S(SymbolicPredicate.T);
    object = SymbolicPredicate.S(SymbolicPredicate.F);
    object = SymbolicPredicate.S(SymbolicPredicate.T);
    object = SymbolicPredicate.S(SymbolicPredicate.F);
    // of() returns not null
    assert SymbolicPredicate.S(SymbolicPredicate.T) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.F) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.T) != null;
    assert SymbolicPredicate.S(SymbolicPredicate.F) != null;
    // of() is of type condition
    condition = SymbolicPredicate.S(SymbolicPredicate.T);
    condition = SymbolicPredicate.S(SymbolicPredicate.F);
    condition = SymbolicPredicate.S(SymbolicPredicate.T);
    condition = SymbolicPredicate.S(SymbolicPredicate.F);
    // make sure that of() is reasonably behaved
    assert SymbolicPredicate.S(() -> true).getAsBoolean();
    assert SymbolicPredicate.S(() -> condition != null).getAsBoolean();
    assert !SymbolicPredicate.S(() -> false).getAsBoolean();
    assert SymbolicPredicate.S(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert SymbolicPredicate.S(SymbolicPredicate.T).getAsBoolean();
    assert !SymbolicPredicate.S(SymbolicPredicate.F).getAsBoolean();
    assert SymbolicPredicate.S(SymbolicPredicate.T).getAsBoolean();
    assert !SymbolicPredicate.S(SymbolicPredicate.F).getAsBoolean();
    // Force and() signature
    SymbolicPredicate.AND(T, T);
    SymbolicPredicate.AND(T, T);
    SymbolicPredicate.AND(T, T, T);
    SymbolicPredicate.AND(T, T, T, T);
    assert SymbolicPredicate.AND(T, T).getAsBoolean();
    // Force and() type
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    // Force and() behavior
    assert SymbolicPredicate.AND(T, T).getAsBoolean();
    assert !SymbolicPredicate.AND(T, F).getAsBoolean();
    assert !SymbolicPredicate.AND(F, T).getAsBoolean();
    assert !SymbolicPredicate.AND(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert SymbolicPredicate.AND(T, T, T).getAsBoolean();
    assert !SymbolicPredicate.AND(T, F, T).getAsBoolean();
    assert !SymbolicPredicate.AND(F, T, T).getAsBoolean();
    assert !SymbolicPredicate.AND(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert SymbolicPredicate.AND(T, T, T).getAsBoolean();
    assert !SymbolicPredicate.AND(T, F, X).getAsBoolean();
    assert !SymbolicPredicate.AND(F, X, X).getAsBoolean();
    assert !SymbolicPredicate.AND(F, F, X).getAsBoolean();
    // Force or() signature
    SymbolicPredicate.OR(T, T);
    SymbolicPredicate.OR(T, T);
    SymbolicPredicate.OR(T, T, T);
    SymbolicPredicate.OR(T, T, T, T);
    assert SymbolicPredicate.OR(T, T).getAsBoolean();
    // Force or() type
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    // Force or() behavior
    assert SymbolicPredicate.OR(T, T).getAsBoolean();
    assert SymbolicPredicate.OR(T, F).getAsBoolean();
    assert SymbolicPredicate.OR(F, T).getAsBoolean();
    assert !SymbolicPredicate.OR(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !SymbolicPredicate.OR(F, F, F).getAsBoolean();
    assert SymbolicPredicate.OR(F, F, T).getAsBoolean();
    assert SymbolicPredicate.OR(F, T, F).getAsBoolean();
    assert SymbolicPredicate.OR(F, T, T).getAsBoolean();
    assert SymbolicPredicate.OR(T, F, F).getAsBoolean();
    assert SymbolicPredicate.OR(T, F, T).getAsBoolean();
    assert SymbolicPredicate.OR(T, T, F).getAsBoolean();
    assert SymbolicPredicate.OR(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert SymbolicPredicate.OR(F, T, X).getAsBoolean();
    assert SymbolicPredicate.OR(T, X, X).getAsBoolean();
    assert SymbolicPredicate.OR(T, F, X).getAsBoolean();
    assert SymbolicPredicate.OR(T, X, X).getAsBoolean();
    // Demonstrate not
    assert SymbolicPredicate.NOT(F).getAsBoolean();
    assert !SymbolicPredicate.NOT(T).getAsBoolean();
    // Now some more complex expressions
    assert SymbolicPredicate.NOT(F).and(NOT(F)).getAsBoolean();
    assert !SymbolicPredicate.NOT(F).and(NOT(T)).getAsBoolean();
    assert SymbolicPredicate.NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert SymbolicPredicate.NOT(F).and(NOT(F)).or(T).eval();
    assert SymbolicPredicate.NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert SymbolicPredicate.NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // More fancy syntax.
    assert NOT(F).and(NOT(F)).getAsBoolean();
    assert !NOT(F).and(NOT(T)).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // Check precedence: A || B && C
    assert SymbolicPredicate.S(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  private static boolean ignoreNext() {
    return true;
  }
}
