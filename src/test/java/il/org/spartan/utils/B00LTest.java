package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.B00L.*;

import java.util.function.*;

import org.junit.*;

import il.org.spartan.*;

/** Tests class {@link B00L}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class B00LTest {
  private B00L B1, T_OR_F_OR_X;
  private B00L B2, T_AND_F_AND_X;
  private B00L B3, FNF_OR_X_OR_N_OR_T;
  private B00L B4, T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T;
  private B00L B5, B1_AND_B2;
  private B00L B6, B2_AND_B1;
  private B00L B7, B1_OR_B2;
  private B00L B8, B2_OR_B1;

  @Before public void setUp() {
    B1 = T_OR_F_OR_X = B00L.OR("T OR F OR X", T, F, X);
    B2 = T_AND_F_AND_X = B00L.AND("T AND F AND X", T, F, X);
    B3 = FNF_OR_X_OR_N_OR_T = NOT(F).and(NOT(F)).or(X).or(N, T);
    B4 = T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T = B00L.OR(T_OR_F_OR_X, FNF_OR_X_OR_N_OR_T);
    B5 = B1_AND_B2 = B1.and(B2);
    B6 = B2_AND_B1 = B2.and(B1);
    B7 = B1_OR_B2 = B1.or(B2);
    B8 = B2_OR_B1 = B2.or(B1);
  }

  private static boolean ignoreNext() {
    return true;
  }

  B00L condition;
  B00L inner;
  Object object;
  BooleanSupplier supplier;

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
    B00L.of(B00L.T);
    B00L.of(B00L.F);
    B00L.of(B00L.T);
    B00L.of(B00L.F);
    // of() is not void
    (B00L.of(B00L.T) + "").hashCode();
    (B00L.of(B00L.F) + "").hashCode();
    (B00L.of(B00L.T) + "").hashCode();
    (B00L.of(B00L.F) + "").hashCode();
    // of() returns not null
    assert B00L.of(B00L.T) != null;
    assert B00L.of(B00L.F) != null;
    assert B00L.of(B00L.T) != null;
    assert B00L.of(B00L.F) != null;
    // of() returns an object
    object = B00L.of(B00L.T);
    object = B00L.of(B00L.F);
    object = B00L.of(B00L.T);
    object = B00L.of(B00L.F);
    // of() returns not null
    assert B00L.of(B00L.T) != null;
    assert B00L.of(B00L.F) != null;
    assert B00L.of(B00L.T) != null;
    assert B00L.of(B00L.F) != null;
    // of() is of type condition
    condition = B00L.of(B00L.T);
    condition = B00L.of(B00L.F);
    condition = B00L.of(B00L.T);
    condition = B00L.of(B00L.F);
    // make sure that of() is reasonably behaved
    assert B00L.of(() -> true).getAsBoolean();
    assert B00L.of(() -> condition != null).getAsBoolean();
    assert !B00L.of(() -> false).getAsBoolean();
    assert B00L.of(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert B00L.of(B00L.T).getAsBoolean();
    assert !B00L.of(B00L.F).getAsBoolean();
    assert B00L.of(B00L.T).getAsBoolean();
    assert !B00L.of(B00L.F).getAsBoolean();
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
    assert T_OR_F_OR_X.getAsBoolean();
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
    final B00L or = NOT(F).and(NOT(F)).or(T).or(X, X);
    assert or.eval();
    // Check precedence: A || B && C
    assert B00L.of(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  @Test public void b() {
    azzert.that(T_OR_F_OR_X.reduce(new B00LReducingGear<String>(new ReduceStringConcatenate()) {
      @Override protected String map(@SuppressWarnings("unused") final BooleanSupplier __) {
        return "";
      }
    }), is(""));
  }

  @Test public void c() {
    azzert.that(T_OR_F_OR_X.reduce(new B00LReducingGear<String>(new ReduceStringConcatenate()) {
      @Override protected String map(final BooleanSupplier ¢) {
        return ¢ + "";
      }
    }), is("T_OR_F_OR_X"));
  }

  @Test public void d() {
    azzert.that(T_OR_F_OR_X.reduce(new B00LJavaNotation()), is("T || F || X"));
  }

  @Test public void e() {
    azzert.that(T_AND_F_AND_X.reduce(new B00LJavaNotation()), is("T || F || X"));
  }

  @Test public void f() {
    azzert.that(FNF_OR_X_OR_N_OR_T.reduce(new B00LJavaNotation()), is("!F && !F || X || N || T"));
  }

  @Test public void g() {
    azzert.that(T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T.reduce(new B00LJavaNotation()), is("T && F && X && (!F && !F || X || N || T)"));
  }
}
