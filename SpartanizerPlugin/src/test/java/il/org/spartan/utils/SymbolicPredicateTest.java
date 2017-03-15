package il.org.spartan.utils;

import static il.org.spartan.utils.BOOL.*;

import java.util.function.*;

import org.junit.*;

/** Tests class {@link BOOL}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class SymbolicPredicateTest {
  Object object;
  BooleanSupplier supplier;
  BOOL condition;
  BOOL inner;

  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }

  @Test public void aa() {
    object = BOOL.T;
    object = BOOL.F;
    object = BOOL.T;
    object = BOOL.F;
    assert BOOL.T != null;
    assert BOOL.T != null;
    assert BOOL.F != null;
    assert BOOL.T != null;
    assert BOOL.F != null;
    supplier = BOOL.T;
    supplier = BOOL.F;
    supplier = BOOL.T;
    supplier = BOOL.F;
    assert ignoreNext() || BOOL.T.getAsBoolean();
    assert ignoreNext() || BOOL.F.getAsBoolean();
    assert ignoreNext() || BOOL.T.getAsBoolean();
    assert ignoreNext() || BOOL.F.getAsBoolean();
    assert BOOL.T.getAsBoolean();
    assert !BOOL.F.getAsBoolean();
    assert BOOL.T.getAsBoolean();
    assert !BOOL.F.getAsBoolean();
    // of() exists
    BOOL.S(BOOL.T);
    BOOL.S(BOOL.F);
    BOOL.S(BOOL.T);
    BOOL.S(BOOL.F);
    // of() is not void
    (BOOL.S(BOOL.T) + "").hashCode();
    (BOOL.S(BOOL.F) + "").hashCode();
    (BOOL.S(BOOL.T) + "").hashCode();
    (BOOL.S(BOOL.F) + "").hashCode();
    // of() returns not null
    assert BOOL.S(BOOL.T) != null;
    assert BOOL.S(BOOL.F) != null;
    assert BOOL.S(BOOL.T) != null;
    assert BOOL.S(BOOL.F) != null;
    // of() returns an object
    object = BOOL.S(BOOL.T);
    object = BOOL.S(BOOL.F);
    object = BOOL.S(BOOL.T);
    object = BOOL.S(BOOL.F);
    // of() returns not null
    assert BOOL.S(BOOL.T) != null;
    assert BOOL.S(BOOL.F) != null;
    assert BOOL.S(BOOL.T) != null;
    assert BOOL.S(BOOL.F) != null;
    // of() is of type condition
    condition = BOOL.S(BOOL.T);
    condition = BOOL.S(BOOL.F);
    condition = BOOL.S(BOOL.T);
    condition = BOOL.S(BOOL.F);
    // make sure that of() is reasonably behaved
    assert BOOL.S(() -> true).getAsBoolean();
    assert BOOL.S(() -> condition != null).getAsBoolean();
    assert !BOOL.S(() -> false).getAsBoolean();
    assert BOOL.S(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert BOOL.S(BOOL.T).getAsBoolean();
    assert !BOOL.S(BOOL.F).getAsBoolean();
    assert BOOL.S(BOOL.T).getAsBoolean();
    assert !BOOL.S(BOOL.F).getAsBoolean();
    // Force and() signature
    BOOL.AND(T, T);
    BOOL.AND(T, T);
    BOOL.AND(T, T, T);
    BOOL.AND(T, T, T, T);
    assert BOOL.AND(T, T).getAsBoolean();
    // Force and() type
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    // Force and() behavior
    assert BOOL.AND(T, T).getAsBoolean();
    assert !BOOL.AND(T, F).getAsBoolean();
    assert !BOOL.AND(F, T).getAsBoolean();
    assert !BOOL.AND(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert BOOL.AND(T, T, T).getAsBoolean();
    assert !BOOL.AND(T, F, T).getAsBoolean();
    assert !BOOL.AND(F, T, T).getAsBoolean();
    assert !BOOL.AND(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert BOOL.AND(T, T, T).getAsBoolean();
    assert !BOOL.AND(T, F, X).getAsBoolean();
    assert !BOOL.AND(F, X, X).getAsBoolean();
    assert !BOOL.AND(F, F, X).getAsBoolean();
    // Force or() signature
    BOOL.OR(T, T);
    BOOL.OR(T, T);
    BOOL.OR(T, T, T);
    BOOL.OR(T, T, T, T);
    assert BOOL.OR(T, T).getAsBoolean();
    // Force or() type
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    // Force or() behavior
    assert BOOL.OR(T, T).getAsBoolean();
    assert BOOL.OR(T, F).getAsBoolean();
    assert BOOL.OR(F, T).getAsBoolean();
    assert !BOOL.OR(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !BOOL.OR(F, F, F).getAsBoolean();
    assert BOOL.OR(F, F, T).getAsBoolean();
    assert BOOL.OR(F, T, F).getAsBoolean();
    assert BOOL.OR(F, T, T).getAsBoolean();
    assert BOOL.OR(T, F, F).getAsBoolean();
    assert BOOL.OR(T, F, T).getAsBoolean();
    assert BOOL.OR(T, T, F).getAsBoolean();
    assert BOOL.OR(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert BOOL.OR(F, T, X).getAsBoolean();
    assert BOOL.OR(T, X, X).getAsBoolean();
    assert BOOL.OR(T, F, X).getAsBoolean();
    assert BOOL.OR(T, X, X).getAsBoolean();
    // Demonstrate not
    assert BOOL.NOT(F).getAsBoolean();
    assert !BOOL.NOT(T).getAsBoolean();
    // Now some more complex expressions
    assert BOOL.NOT(F).and(NOT(F)).getAsBoolean();
    assert !BOOL.NOT(F).and(NOT(T)).getAsBoolean();
    assert BOOL.NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert BOOL.NOT(F).and(NOT(F)).or(T).eval();
    assert BOOL.NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert BOOL.NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // More fancy syntax.
    assert NOT(F).and(NOT(F)).getAsBoolean();
    assert !NOT(F).and(NOT(T)).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // Check precedence: A || B && C
    assert BOOL.S(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  private static boolean ignoreNext() {
    return true;
  }
}
