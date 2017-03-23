package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Proposition.*;

import java.util.*;
import java.util.function.*;

import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Tests class {@link Proposition}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("Under construction")
@SuppressWarnings("static-method")
public class PropositionTest {
  private static boolean ignoreNext() {
    return true;
  }

  private Proposition B1, T_OR_F_OR_X;
  private Proposition B2, T_AND_F_AND_X;
  private Proposition B3, FNF_OR_X_OR_N_OR_T;
  private Proposition B4, T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T;
  private Proposition B5, B1_AND_B2;
  private Proposition B6, B2_AND_B1;
  private Proposition B7, B1_OR_B2;
  private Proposition B8, B2_OR_B1;
  Proposition condition;
  Proposition inner;
  private final PropositionJavaNotation java = new PropositionJavaNotation();
  Object object;
  BooleanSupplier supplier;

  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }

  @Test public void j() {
    azzert.that(B3.reduce(java), is(""));
    azzert.that(FNF_OR_X_OR_N_OR_T.reduce(java), is(""));
    azzert.that(B4.reduce(java), is(""));
    azzert.that(T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T.reduce(java), is(""));
    azzert.that(B5.reduce(java), is(""));
    azzert.that(B1_AND_B2.reduce(java), is(""));
    azzert.that(B6.reduce(java), is(""));
    azzert.that(B2_AND_B1.reduce(java), is(""));
    azzert.that(B7.reduce(java), is(""));
    azzert.that(B1_OR_B2.reduce(java), is(""));
    azzert.that(B8.reduce(java), is(""));
    azzert.that(B2_OR_B1.reduce(java), is(""));
  }

  /**
   * [[SuppressWarningsSpartan]]
   */
  @Test public void aa() {
    object = Proposition.T;
    assert object != null;
    object = Proposition.F;
    object = Proposition.N;
    object = Proposition.X;
    assert Proposition.T != null;
    assert Proposition.T != null;
    assert Proposition.F != null;
    assert Proposition.T != null;
    assert Proposition.F != null;
    supplier = Proposition.T;
    supplier = Proposition.F;
    supplier = Proposition.T;
    supplier = Proposition.F;
    assert ignoreNext() || Proposition.T.getAsBoolean();
    assert ignoreNext() || Proposition.F.getAsBoolean();
    assert ignoreNext() || Proposition.T.getAsBoolean();
    assert ignoreNext() || Proposition.F.getAsBoolean();
    assert Proposition.T.getAsBoolean();
    assert !Proposition.F.getAsBoolean();
    assert Proposition.T.getAsBoolean();
    assert !Proposition.F.getAsBoolean();
    // of() exists
    Proposition.of(Proposition.T);
    Proposition.of(Proposition.F);
    Proposition.of(Proposition.T);
    Proposition.of(Proposition.F);
    // of() is not void
    (Proposition.of(Proposition.T) + "").hashCode();
    (Proposition.of(Proposition.F) + "").hashCode();
    (Proposition.of(Proposition.T) + "").hashCode();
    (Proposition.of(Proposition.F) + "").hashCode();
    // of() returns not null
    assert Proposition.of(Proposition.T) != null;
    assert Proposition.of(Proposition.F) != null;
    assert Proposition.of(Proposition.T) != null;
    assert Proposition.of(Proposition.F) != null;
    // of() returns an object
    object = Proposition.of(Proposition.T);
    object = Proposition.of(Proposition.F);
    object = Proposition.of(Proposition.T);
    object = Proposition.of(Proposition.F);
    // of() returns not null
    assert Proposition.of(Proposition.T) != null;
    assert Proposition.of(Proposition.F) != null;
    assert Proposition.of(Proposition.T) != null;
    assert Proposition.of(Proposition.F) != null;
    // of() is of type condition
    condition = Proposition.of(Proposition.T);
    condition = Proposition.of(Proposition.F);
    condition = Proposition.of(Proposition.T);
    condition = Proposition.of(Proposition.F);
    // make sure that of() is reasonably behaved
    assert Proposition.of(() -> true).getAsBoolean();
    assert Proposition.of(() -> condition != null).getAsBoolean();
    assert !Proposition.of(() -> false).getAsBoolean();
    assert Proposition.of(() -> hashCode() == hashCode()).getAsBoolean();
    // make sure that of() is also correct on our four constants:
    assert Proposition.of(Proposition.T).getAsBoolean();
    assert !Proposition.of(Proposition.F).getAsBoolean();
    assert Proposition.of(Proposition.T).getAsBoolean();
    assert !Proposition.of(Proposition.F).getAsBoolean();
    // Force and() signature
    Proposition.AND(T, T);
    Proposition.AND(T, T);
    Proposition.AND(T, T, T);
    Proposition.AND(T, T, T, T);
    assert Proposition.AND(T, T).getAsBoolean();
    // Force and() type
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    // Force and() behavior
    assert Proposition.AND(T, T).getAsBoolean();
    assert !Proposition.AND(T, F).getAsBoolean();
    assert !Proposition.AND(F, T).getAsBoolean();
    assert !Proposition.AND(F, F).getAsBoolean();
    // Force and() semantic ternary
    assert Proposition.AND(T, T, T).getAsBoolean();
    assert !Proposition.AND(T, F, T).getAsBoolean();
    assert !Proposition.AND(F, T, T).getAsBoolean();
    assert !Proposition.AND(F, F, T).getAsBoolean();
    // Force and() short circuit
    assert Proposition.AND(T, T, T).getAsBoolean();
    assert !Proposition.AND(T, F, X).getAsBoolean();
    assert !Proposition.AND(F, X, X).getAsBoolean();
    assert !Proposition.AND(F, F, X).getAsBoolean();
    // Force or() signature
    Proposition.OR(T, T);
    Proposition.OR(T, T);
    Proposition.OR(T, T, T);
    Proposition.OR(T, T, T, T);
    assert Proposition.OR(T, T).getAsBoolean();
    // Force or() type
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    // Force or() behavior
    assert Proposition.OR(T, T).getAsBoolean();
    assert Proposition.OR(T, F).getAsBoolean();
    assert Proposition.OR(F, T).getAsBoolean();
    assert !Proposition.OR(F, F).getAsBoolean();
    // Force or() semantic ternary
    assert !Proposition.OR(F, F, F).getAsBoolean();
    assert Proposition.OR(F, F, T).getAsBoolean();
    assert Proposition.OR(F, T, F).getAsBoolean();
    assert Proposition.OR(F, T, T).getAsBoolean();
    assert Proposition.OR(T, F, F).getAsBoolean();
    assert Proposition.OR(T, F, T).getAsBoolean();
    assert Proposition.OR(T, T, F).getAsBoolean();
    assert Proposition.OR(T, T, T).getAsBoolean();
    // Force or() short circuit
    assert Proposition.OR(F, T, X).getAsBoolean();
    assert Proposition.OR(T, X, X).getAsBoolean();
    assert T_OR_F_OR_X.getAsBoolean();
    assert Proposition.OR(T, X, X).getAsBoolean();
    // Demonstrate not
    assert Proposition.NOT(F).getAsBoolean();
    assert !Proposition.NOT(T).getAsBoolean();
    // Now some more complex expressions
    assert Proposition.NOT(F).and(NOT(F)).getAsBoolean();
    assert !Proposition.NOT(F).and(NOT(T)).getAsBoolean();
    assert Proposition.NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert Proposition.NOT(F).and(NOT(F)).or(T).eval();
    assert Proposition.NOT(F).and(NOT(F)).or(T).or(X).eval();
    assert Proposition.NOT(F).and(NOT(F)).or(T).or(X, X).eval();
    // More fancy syntax.
    assert NOT(F).and(NOT(F)).getAsBoolean();
    assert !NOT(F).and(NOT(T)).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).getAsBoolean();
    assert NOT(F).and(NOT(F)).or(T).eval();
    assert NOT(F).and(NOT(F)).or(T).or(X).eval();
    @NotNull final Proposition or = NOT(F).and(NOT(F)).or(T).or(X, X);
    assert or.eval();
    // Check precedence: A || B && C
    assert Proposition.of(F).or(T).and(T).eval();
    // Check precedence: (A || B) && C
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }

  @Test public void z() {
    azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
      @Override @NotNull protected String map(@SuppressWarnings("unused") final BooleanSupplier __) {
        return "";
      }
    }), is(""));
  }

  @Test public void z1() {
    assert T_OR_F_OR_X.eval();
  }

  @Test public void b2() {
    azzert.that(T_OR_F_OR_X + "", is("T OR F OR X"));
  }

  @Test public void b3() {
    assert !hasCycles(T);
    assert !hasCycles(X);
    assert !hasCycles(F);
    assert !hasCycles(N);
    assert !hasCycles(T.and(F));
    assert !hasCycles(F.and(T));
    assert !hasCycles(T.or(T));
    assert !hasCycles(T.or(T));
    assert !hasCycles(T_AND_F_AND_X);
    assert !hasCycles(T_OR_F_OR_X);
  }

  @Test public void b31() {
    assert !hasCycles(T.or(T));
  }

  @Test public void b4() {
    T.reduce(java);
  }

  @Test public void b5() {
    F.and(F).reduce(java);
  }

  @Test public void b8() {
    T.or(X).reduce(java);
  }

  @Test public void b9() {
    azzert.that(T.or(X).reduce(java), is("T || X"));
  }

  @Test public void b91() {
    azzert.that(T.and(X).reduce(java), is("T && X"));
  }

  @Test public void b92() {
    azzert.that(NOT(T).reduce(java), is("!T"));
  }

  @Test public void b93() {
    azzert.that(NOT(X).reduce(java), is("!X"));
  }

  @Test public void b94() {
    azzert.that(NOT(N).reduce(java), is("!N"));
  }

  @Test public void b95() {
    azzert.that(of(T).reduce(java), is("T"));
  }

  @Test public void d() {
    azzert.that(T_OR_F_OR_X.reduce(java), is("T || F || X"));
  }

  @Test public void d0() {
    T.or(X, X).reduce(java);
    azzert.that(T.or(X, X).reduce(java), is("T || X || X"));
  }

  @Test public void d1() {
    azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
      @Override @NotNull protected String map(final BooleanSupplier ¢) {
        return ¢ + "";
      }
    }), is("T_OR_F_OR_X"));
  }

  @Test public void e() {
    azzert.that(T_AND_F_AND_X.reduce(java), is("T || F || X"));
  }

  @Test public void f() {
    azzert.that(FNF_OR_X_OR_N_OR_T.reduce(java), is("!F && !F || X || N || T"));
  }

  @Test public void f0() {
    azzert.that(T + "", is("T"));
    azzert.that(F + "", is("F"));
    azzert.that(N + "", is("N"));
    azzert.that(X + "", is("X"));
  }

  @Test public void g() {
    azzert.that(T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T.reduce(java), is("T && F && X && (!F && !F || X || N || T)"));
  }

  private boolean hasCycles(final BooleanSupplier s) {
    @NotNull final Stack<BooleanSupplier> stack = new Stack<>();
    stack.add(s);
    do {
      final BooleanSupplier current = stack.pop();
      if (stack.contains(current))
        return true;
      if (current instanceof Proposition.C) {
        stack.addAll(((Proposition.C) current).inner);
        continue;
      }
      if (current instanceof Proposition.P) {
        stack.push(((Proposition.P) current).inner);
        continue;
      }
    } while (!stack.isEmpty());
    return false;
  }

  @Before public void setUp() {
    B1 = T_OR_F_OR_X = Proposition.OR("T OR F OR X", T, F, X);
    B2 = T_AND_F_AND_X = Proposition.AND("T AND F AND X", T, F, X);
    B3 = FNF_OR_X_OR_N_OR_T = NOT(F).and(NOT(F)).or(X).or(N, T);
    B4 = T_OR_F_OR_X_AND_FNF_OR_X_OR_N_OR_T = Proposition.OR(T_OR_F_OR_X, FNF_OR_X_OR_N_OR_T);
    B5 = B1_AND_B2 = B1.and(B2);
    B6 = B2_AND_B1 = B2.and(B1);
    B7 = B1_OR_B2 = B1.or(B2);
    B8 = B2_OR_B1 = B2.or(B1);
  }
}
