package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Trivalent.*;
import static il.org.spartan.utils.Trivalent.not;

import java.util.function.*;

import org.junit.*;

import il.org.spartan.*;

/** Tests class {@link Trivalent}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class B00LTest {
  private static final Trivalent FIXTURE = Trivalent.OR(T, F, X);

  private static boolean ignoreNext() {
    return true;
  }

  Trivalent condition;
  Trivalent inner;
  Object object;
  BooleanSupplier supplier;

  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }

  @Test public void aa() {
    object = Trivalent.T;
    object = Trivalent.F;
    object = Trivalent.T;
    object = Trivalent.F;
    assert Trivalent.T != null;
    assert Trivalent.T != null;
    assert Trivalent.F != null;
    assert Trivalent.T != null;
    assert Trivalent.F != null;
    supplier = Trivalent.T;
    supplier = Trivalent.F;
    supplier = Trivalent.T;
    supplier = Trivalent.F;
    assert ignoreNext() || Trivalent.T.getAsBoolean();
    assert ignoreNext() || Trivalent.F.getAsBoolean();
    assert ignoreNext() || Trivalent.T.getAsBoolean();
    assert ignoreNext() || Trivalent.F.getAsBoolean();
    assert Trivalent.T.getAsBoolean();
    assert !Trivalent.F.getAsBoolean();
    assert Trivalent.T.getAsBoolean();
    assert !Trivalent.F.getAsBoolean();
    // of() exists
    Trivalent.of(Trivalent.T);
    Trivalent.of(Trivalent.F);
    Trivalent.of(Trivalent.T);
    Trivalent.of(Trivalent.F);
    // of() is not void
    (Trivalent.of(Trivalent.T) + "").hashCode();
    (Trivalent.of(Trivalent.F) + "").hashCode();
    (Trivalent.of(Trivalent.T) + "").hashCode();
    (Trivalent.of(Trivalent.F) + "").hashCode();
    // of() returns not null
    assert Trivalent.of(Trivalent.T) != null;
    assert Trivalent.of(Trivalent.F) != null;
    assert Trivalent.of(Trivalent.T) != null;
    assert Trivalent.of(Trivalent.F) != null;
    // of() returns an object
    object = Trivalent.of(Trivalent.T);
    object = Trivalent.of(Trivalent.F);
    object = Trivalent.of(Trivalent.T);
    object = Trivalent.of(Trivalent.F);
    // of() returns not null
    assert Trivalent.of(Trivalent.T) != null;
    assert Trivalent.of(Trivalent.F) != null;
    assert Trivalent.of(Trivalent.T) != null;
    assert Trivalent.of(Trivalent.F) != null;
    // of() is of type condition
    condition = Trivalent.of(Trivalent.T);
    condition = Trivalent.of(Trivalent.F);
    condition = Trivalent.of(Trivalent.T);
    condition = Trivalent.of(Trivalent.F);
    // make sure that of() is reasonably behaved
    assert Trivalent.of(() -> true).getAsBoolean();
    assert Trivalent.of(() -> condition != null).getAsBoolean();
    assert !Trivalent.of(() -> false).getAsBoolean();
    assert Trivalent.of(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert Trivalent.of(Trivalent.T).getAsBoolean();
    assert !Trivalent.of(Trivalent.F).getAsBoolean();
    assert Trivalent.of(Trivalent.T).getAsBoolean();
    assert !Trivalent.of(Trivalent.F).getAsBoolean();
    // Force and() signature
    Trivalent.AND(T, T);
    Trivalent.AND(T, T);
    Trivalent.AND(T, T, T);
    Trivalent.AND(T, T, T, T);
    assert Trivalent.AND(T, T).getAsBoolean();
    // Force and() type
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    // Force and() behavior
    assert Trivalent.AND(T, T).getAsBoolean();
    assert !Trivalent.AND(T, F).getAsBoolean();
    assert !Trivalent.AND(F, T).getAsBoolean();
    assert !Trivalent.AND(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert Trivalent.AND(T, T, T).getAsBoolean();
    assert !Trivalent.AND(T, F, T).getAsBoolean();
    assert !Trivalent.AND(F, T, T).getAsBoolean();
    assert !Trivalent.AND(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert Trivalent.AND(T, T, T).getAsBoolean();
    assert !Trivalent.AND(T, F, X).getAsBoolean();
    assert !Trivalent.AND(F, X, X).getAsBoolean();
    assert !Trivalent.AND(F, F, X).getAsBoolean();
    // Force or() signature
    Trivalent.OR(T, T);
    Trivalent.OR(T, T);
    Trivalent.OR(T, T, T);
    Trivalent.OR(T, T, T, T);
    assert Trivalent.OR(T, T).getAsBoolean();
    // Force or() type
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    // Force or() behavior
    assert Trivalent.OR(T, T).getAsBoolean();
    assert Trivalent.OR(T, F).getAsBoolean();
    assert Trivalent.OR(F, T).getAsBoolean();
    assert !Trivalent.OR(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !Trivalent.OR(F, F, F).getAsBoolean();
    assert Trivalent.OR(F, F, T).getAsBoolean();
    assert Trivalent.OR(F, T, F).getAsBoolean();
    assert Trivalent.OR(F, T, T).getAsBoolean();
    assert Trivalent.OR(T, F, F).getAsBoolean();
    assert Trivalent.OR(T, F, T).getAsBoolean();
    assert Trivalent.OR(T, T, F).getAsBoolean();
    assert Trivalent.OR(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert Trivalent.OR(F, T, X).getAsBoolean();
    assert Trivalent.OR(T, X, X).getAsBoolean();
    assert FIXTURE.getAsBoolean();
    assert Trivalent.OR(T, X, X).getAsBoolean();
    // Demonstrate not
    assert Trivalent.not(F).getAsBoolean();
    assert !Trivalent.not(T).getAsBoolean();
    // Now some more complex expressions
    assert Trivalent.not(F).and(not(F)).getAsBoolean();
    assert !Trivalent.not(F).and(not(T)).getAsBoolean();
    assert Trivalent.not(F).and(not(F)).or(T).getAsBoolean();
    assert Trivalent.not(F).and(not(F)).or(T).eval();
    assert Trivalent.not(F).and(not(F)).or(T).or(X).eval();
    assert Trivalent.not(F).and(not(F)).or(T).or(X, X).eval();
    // More fancy syntax.
    assert not(F).and(not(F)).getAsBoolean();
    assert !not(F).and(not(T)).getAsBoolean();
    assert not(F).and(not(F)).or(T).getAsBoolean();
    assert not(F).and(not(F)).or(T).eval();
    assert not(F).and(not(F)).or(T).or(X).eval();
    assert not(F).and(not(F)).or(T).or(X, X).eval();
    // Check precedence: A || B && C
    assert Trivalent.of(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  @Test public void b() {
    azzert.that(FIXTURE.reduce(new ReducingGear<String>(new ReduceStringConcatenate()) {
      @Override protected String map(@SuppressWarnings("unused") final BooleanSupplier __) {
        return "";
      }
    }), is(""));
  }
}
