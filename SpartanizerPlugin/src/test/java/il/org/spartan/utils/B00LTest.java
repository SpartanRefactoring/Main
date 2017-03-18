package il.org.spartan.utils;

import static il.org.spartan.utils.B00L.*;

import java.util.function.*;

import org.junit.*;

/** Tests class {@link B00L}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class B00LTest {
  Object object;
  BooleanSupplier supplier;
  B00L condition;
  B00L inner;

  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }

  @Test public void aa() {
    object = B00L.T;
    object = B00L.F;
    object = B00L.T;
    object = B00L.F;
    assert B00L.T != null;
    assert B00L.T != null;
    assert B00L.F != null;
    assert B00L.T != null;
    assert B00L.F != null;
    supplier = B00L.T;
    supplier = B00L.F;
    supplier = B00L.T;
    supplier = B00L.F;
    assert ignoreNext() || B00L.T.getAsBoolean();
    assert ignoreNext() || B00L.F.getAsBoolean();
    assert ignoreNext() || B00L.T.getAsBoolean();
    assert ignoreNext() || B00L.F.getAsBoolean();
    assert B00L.T.getAsBoolean();
    assert !B00L.F.getAsBoolean();
    assert B00L.T.getAsBoolean();
    assert !B00L.F.getAsBoolean();
    // of() exists
    B00L.S(B00L.T);
    B00L.S(B00L.F);
    B00L.S(B00L.T);
    B00L.S(B00L.F);
    // of() is not void
    (B00L.S(B00L.T) + "").hashCode();
    (B00L.S(B00L.F) + "").hashCode();
    (B00L.S(B00L.T) + "").hashCode();
    (B00L.S(B00L.F) + "").hashCode();
    // of() returns not null
    assert B00L.S(B00L.T) != null;
    assert B00L.S(B00L.F) != null;
    assert B00L.S(B00L.T) != null;
    assert B00L.S(B00L.F) != null;
    // of() returns an object
    object = B00L.S(B00L.T);
    object = B00L.S(B00L.F);
    object = B00L.S(B00L.T);
    object = B00L.S(B00L.F);
    // of() returns not null
    assert B00L.S(B00L.T) != null;
    assert B00L.S(B00L.F) != null;
    assert B00L.S(B00L.T) != null;
    assert B00L.S(B00L.F) != null;
    // of() is of type condition
    condition = B00L.S(B00L.T);
    condition = B00L.S(B00L.F);
    condition = B00L.S(B00L.T);
    condition = B00L.S(B00L.F);
    // make sure that of() is reasonably behaved
    assert B00L.S(() -> true).getAsBoolean();
    assert B00L.S(() -> condition != null).getAsBoolean();
    assert !B00L.S(() -> false).getAsBoolean();
    assert B00L.S(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert B00L.S(B00L.T).getAsBoolean();
    assert !B00L.S(B00L.F).getAsBoolean();
    assert B00L.S(B00L.T).getAsBoolean();
    assert !B00L.S(B00L.F).getAsBoolean();
    // Force and() signature
    B00L.AND(T, T);
    B00L.AND(T, T);
    B00L.AND(T, T, T);
    B00L.AND(T, T, T, T);
    assert B00L.AND(T, T).getAsBoolean();
    // Force and() type
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    // Force and() behavior
    assert B00L.AND(T, T).getAsBoolean();
    assert !B00L.AND(T, F).getAsBoolean();
    assert !B00L.AND(F, T).getAsBoolean();
    assert !B00L.AND(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert B00L.AND(T, T, T).getAsBoolean();
    assert !B00L.AND(T, F, T).getAsBoolean();
    assert !B00L.AND(F, T, T).getAsBoolean();
    assert !B00L.AND(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert B00L.AND(T, T, T).getAsBoolean();
    assert !B00L.AND(T, F, X).getAsBoolean();
    assert !B00L.AND(F, X, X).getAsBoolean();
    assert !B00L.AND(F, F, X).getAsBoolean();
    // Force or() signature
    B00L.OR(T, T);
    B00L.OR(T, T);
    B00L.OR(T, T, T);
    B00L.OR(T, T, T, T);
    assert B00L.OR(T, T).getAsBoolean();
    // Force or() type
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    // Force or() behavior
    assert B00L.OR(T, T).getAsBoolean();
    assert B00L.OR(T, F).getAsBoolean();
    assert B00L.OR(F, T).getAsBoolean();
    assert !B00L.OR(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !B00L.OR(F, F, F).getAsBoolean();
    assert B00L.OR(F, F, T).getAsBoolean();
    assert B00L.OR(F, T, F).getAsBoolean();
    assert B00L.OR(F, T, T).getAsBoolean();
    assert B00L.OR(T, F, F).getAsBoolean();
    assert B00L.OR(T, F, T).getAsBoolean();
    assert B00L.OR(T, T, F).getAsBoolean();
    assert B00L.OR(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert B00L.OR(F, T, X).getAsBoolean();
    assert B00L.OR(T, X, X).getAsBoolean();
    assert B00L.OR(T, F, X).getAsBoolean();
    assert B00L.OR(T, X, X).getAsBoolean();
    // Demonstrate not
    assert B00L.NOT(F).getAsBoolean();
    assert !B00L.NOT(T).getAsBoolean();
    // Now some more complex expressions
    assert B00L.NOT(F).and(NOT(F)).getAsBoolean();
    assert !B00L.NOT(F).and(NOT(T)).getAsBoolean();
    assert B00L.NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert B00L.NOT(F).and(NOT(F)).or(T).eval();
    assert B00L.NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert B00L.NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // More fancy syntax.
    assert NOT(F).and(NOT(F)).getAsBoolean();
    assert !NOT(F).and(NOT(T)).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // Check precedence: A || B && C
    assert B00L.S(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  private static boolean ignoreNext() {
    return true;
  }
}
