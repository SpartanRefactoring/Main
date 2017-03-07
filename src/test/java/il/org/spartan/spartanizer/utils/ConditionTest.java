package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.utils.Condition.*;

import java.util.function.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class ConditionTest {
  Object object;
  BooleanSupplier supplier;
  Condition condition;
  Condition.Inner inner;

  @Test public void a() {
    X.getAsBoolean();
  }

  @Test public void aa() {
    object = Condition.T;
    object = Condition.F;
    object = Condition.T;
    object = Condition.F;
    assert Condition.T != null;
    assert Condition.T != null;
    assert Condition.F != null;
    assert Condition.T != null;
    assert Condition.F != null;
    supplier = Condition.T;
    supplier = Condition.F;
    supplier = Condition.T;
    supplier = Condition.F;
    assert ignoreNext() || Condition.T.getAsBoolean();
    assert ignoreNext() || Condition.F.getAsBoolean();
    assert ignoreNext() || Condition.T.getAsBoolean();
    assert ignoreNext() || Condition.F.getAsBoolean();
    assert Condition.T.getAsBoolean();
    assert !Condition.F.getAsBoolean();
    assert Condition.T.getAsBoolean();
    assert !Condition.F.getAsBoolean();
    // of() exists
    Condition.of(Condition.T);
    Condition.of(Condition.F);
    Condition.of(Condition.T);
    Condition.of(Condition.F);
    // of() is not void
    (Condition.of(Condition.T) + "").hashCode();
    (Condition.of(Condition.F) + "").hashCode();
    (Condition.of(Condition.T) + "").hashCode();
    (Condition.of(Condition.F) + "").hashCode();
    // of() returns not null
    assert Condition.of(Condition.T) != null;
    assert Condition.of(Condition.F) != null;
    assert Condition.of(Condition.T) != null;
    assert Condition.of(Condition.F) != null;
    // of() returns an object
    object = Condition.of(Condition.T);
    object = Condition.of(Condition.F);
    object = Condition.of(Condition.T);
    object = Condition.of(Condition.F);
    // of() returns not null
    assert Condition.of(Condition.T) != null;
    assert Condition.of(Condition.F) != null;
    assert Condition.of(Condition.T) != null;
    assert Condition.of(Condition.F) != null;
    // of() is of type condition
    condition = Condition.of(Condition.T);
    condition = Condition.of(Condition.F);
    condition = Condition.of(Condition.T);
    condition = Condition.of(Condition.F);
    // make sure that of() is reasonably behaved
    assert Condition.of(() -> true).getAsBoolean();
    assert Condition.of(() -> condition != null).getAsBoolean();
    assert !Condition.of(() -> false).getAsBoolean();
    assert Condition.of(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert Condition.of(Condition.T).getAsBoolean();
    assert !Condition.of(Condition.F).getAsBoolean();
    assert Condition.of(Condition.T).getAsBoolean();
    assert !Condition.of(Condition.F).getAsBoolean();
    // Force and() signature
    Condition.and(T, T);
    Condition.and(T, T);
    Condition.and(T, T, T);
    Condition.and(T, T, T, T);
    assert Condition.and(T, T).getAsBoolean();
    // Force and() type
    supplier = and(T, T);
    condition = and(T, T);
    inner = and(T, T);
    // Force and() behavior
    assert Condition.and(T, T).getAsBoolean();
    assert !Condition.and(T, F).getAsBoolean();
    assert !Condition.and(F, T).getAsBoolean();
    assert !Condition.and(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert Condition.and(T, T, T).getAsBoolean();
    assert !Condition.and(T, F, T).getAsBoolean();
    assert !Condition.and(F, T, T).getAsBoolean();
    assert !Condition.and(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert Condition.and(T, T, T).getAsBoolean();
    assert !Condition.and(T, F, X).getAsBoolean();
    assert !Condition.and(F, X, X).getAsBoolean();
    assert !Condition.and(F, F, X).getAsBoolean();
    // Force or() signature
    Condition.or(T, T);
    Condition.or(T, T);
    Condition.or(T, T, T);
    Condition.or(T, T, T, T);
    assert Condition.or(T, T).getAsBoolean();
    // Force or() type
    supplier = or(T, T);
    condition = or(T, T);
    inner = or(T, T);
    // Force or() behavior
    assert Condition.or(T, T).getAsBoolean();
    assert Condition.or(T, F).getAsBoolean();
    assert Condition.or(F, T).getAsBoolean();
    assert !Condition.or(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !Condition.or(F, F, F).getAsBoolean();
    assert Condition.or(F, F, T).getAsBoolean();
    assert Condition.or(F, T, F).getAsBoolean();
    assert Condition.or(F, T, T).getAsBoolean();
    assert Condition.or(T, F, F).getAsBoolean();
    assert Condition.or(T, F, T).getAsBoolean();
    assert Condition.or(T, T, F).getAsBoolean();
    assert Condition.or(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert Condition.or(F, T, X).getAsBoolean();
    assert Condition.or(T, X, X).getAsBoolean();
    assert Condition.or(T, F, X).getAsBoolean();
    assert Condition.or(T, X, X).getAsBoolean();
    // Demonstrate not
    assert Condition.not(F).getAsBoolean();
    assert !Condition.not(T).getAsBoolean();
  }

  private static boolean ignoreNext() {
    return true;
  }
}
