package il.org.spartan.proposition;

import static fluent.ly.azzert.*;
import static il.org.spartan.utils.Proposition.*;
import static il.org.spartan.utils.Proposition.not;
import static il.org.spartan.utils.Proposition.that;

import java.util.*;
import java.util.function.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.utils.*;

/** Tests class {@link Proposition}
 * @author Yossi Gil {@code    Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
@SuppressWarnings("static-method")
public class PropositionTest {
  private static boolean ignoreNext() {
    return true;
  }
  @Test(expected = AssertionError.class) public void a() {
    X.getAsBoolean();
  }
  @Test public void aa() {
    object = Proposition.T;
    assert object != null;
    object = Proposition.F;
    assert object != null;
    object = Proposition.N;
    assert object != null;
    object = Proposition.X;
    assert Proposition.T != null;
    assert Proposition.T != null;
    assert Proposition.F != null;
    assert Proposition.T != null;
    assert Proposition.F != null;
    supplier = Proposition.T;
    assert supplier != null;
    supplier = Proposition.F;
    assert supplier != null;
    supplier = Proposition.T;
    assert supplier != null;
    supplier = Proposition.F;
    assert supplier != null;
    assert ignoreNext() || Proposition.T.getAsBoolean();
    assert ignoreNext() || Proposition.F.getAsBoolean();
    assert ignoreNext() || Proposition.T.getAsBoolean();
    assert ignoreNext() || Proposition.F.getAsBoolean();
    assert Proposition.T.getAsBoolean();
    assert !Proposition.F.getAsBoolean();
    assert Proposition.T.getAsBoolean();
    assert !Proposition.F.getAsBoolean();
    Proposition.that(Proposition.T);
    Proposition.that(Proposition.F);
    Proposition.that(Proposition.T);
    Proposition.that(Proposition.F);
    (Proposition.that(Proposition.T) + "").hashCode();
    (Proposition.that(Proposition.F) + "").hashCode();
    (Proposition.that(Proposition.T) + "").hashCode();
    (Proposition.that(Proposition.F) + "").hashCode();
    assert Proposition.that(Proposition.T) != null;
    assert Proposition.that(Proposition.F) != null;
    assert Proposition.that(Proposition.T) != null;
    assert Proposition.that(Proposition.F) != null;
    object = Proposition.that(Proposition.T);
    object = Proposition.that(Proposition.F);
    object = Proposition.that(Proposition.T);
    object = Proposition.that(Proposition.F);
    assert Proposition.that(Proposition.T) != null;
    assert Proposition.that(Proposition.F) != null;
    assert Proposition.that(Proposition.T) != null;
    assert Proposition.that(Proposition.F) != null;
    condition = Proposition.that(Proposition.T);
    condition = Proposition.that(Proposition.F);
    condition = Proposition.that(Proposition.T);
    condition = Proposition.that(Proposition.F);
    assert Proposition.that(() -> true).getAsBoolean();
    assert Proposition.that(() -> condition != null).getAsBoolean();
    assert !Proposition.that(() -> false).getAsBoolean();
    assert Proposition.that(() -> hashCode() == hashCode()).getAsBoolean();
    assert Proposition.that(Proposition.T).getAsBoolean();
    assert !Proposition.that(Proposition.F).getAsBoolean();
    assert Proposition.that(Proposition.T).getAsBoolean();
    assert !Proposition.that(Proposition.F).getAsBoolean();
    Proposition.AND(T, T);
    Proposition.AND(T, T);
    Proposition.AND(T, T, T);
    Proposition.AND(T, T, T, T);
    assert Proposition.AND(T, T).getAsBoolean();
    supplier = AND(T, T);
    condition = AND(T, T);
    inner = AND(T, T);
    assert Proposition.AND(T, T).getAsBoolean();
    assert !Proposition.AND(T, F).getAsBoolean();
    assert !Proposition.AND(F, T).getAsBoolean();
    assert !Proposition.AND(F, F).getAsBoolean();
    assert Proposition.AND(T, T, T).getAsBoolean();
    assert !Proposition.AND(T, F, T).getAsBoolean();
    assert !Proposition.AND(F, T, T).getAsBoolean();
    assert !Proposition.AND(F, F, T).getAsBoolean();
    assert Proposition.AND(T, T, T).getAsBoolean();
    assert !Proposition.AND(T, F, X).getAsBoolean();
    assert !Proposition.AND(F, X, X).getAsBoolean();
    assert !Proposition.AND(F, F, X).getAsBoolean();
    Proposition.OR(T, T);
    Proposition.OR(T, T);
    Proposition.OR(T, T, T);
    Proposition.OR(T, T, T, T);
    assert Proposition.OR(T, T).getAsBoolean();
    supplier = OR(T, T);
    condition = OR(T, T);
    inner = OR(T, T);
    assert Proposition.OR(T, T).getAsBoolean();
    assert Proposition.OR(T, F).getAsBoolean();
    assert Proposition.OR(F, T).getAsBoolean();
    assert !Proposition.OR(F, F).getAsBoolean();
    assert !Proposition.OR(F, F, F).getAsBoolean();
    assert Proposition.OR(F, F, T).getAsBoolean();
    assert Proposition.OR(F, T, F).getAsBoolean();
    assert Proposition.OR(F, T, T).getAsBoolean();
    assert Proposition.OR(T, F, F).getAsBoolean();
    assert Proposition.OR(T, F, T).getAsBoolean();
    assert Proposition.OR(T, T, F).getAsBoolean();
    assert Proposition.OR(T, T, T).getAsBoolean();
    assert Proposition.OR(F, T, X).getAsBoolean();
    assert Proposition.OR(T, X, X).getAsBoolean();
    assert T_OR_F_OR_X.getAsBoolean();
    assert Proposition.OR(T, X, X).getAsBoolean();
    assert Proposition.not(F).getAsBoolean();
    assert !Proposition.not(T).getAsBoolean();
    assert Proposition.not(F).and(not(F)).getAsBoolean();
    assert !Proposition.not(F).and(not(T)).getAsBoolean();
    assert Proposition.not(F).and(not(F)).or(T).getAsBoolean();
    assert Proposition.not(F).and(not(F)).or(T).eval();
    assert Proposition.not(F).and(not(F)).or(T).or(X).eval();
    assert Proposition.not(F).and(not(F)).or(T).or(X, X).eval();
    assert not(F).and(not(F)).getAsBoolean();
    assert !not(F).and(not(T)).getAsBoolean();
    assert not(F).and(not(F)).or(T).getAsBoolean();
    assert not(F).and(not(F)).or(T).eval();
    assert not(F).and(not(F)).or(T).or(X).eval();
    final Proposition or = not(F).and(not(F)).or(T).or(X, X);
    assert or.eval();
    assert Proposition.that(F).or(T).and(T).eval();
    assert OR(F, T).and(T).eval();
    assert OR(F, T).and(T).or(X).eval();
    assert !OR(F, T).and(T).and(F).eval();
  }
  @Test public void b2() {
    azzert.that(T_OR_F_OR_X + "", is("T OR F OR X"));
    azzert.that(F.or(T).and(T).reduce(javaReducer), is("((F || T) && T)"));
    azzert.that(F.and(X).or(T).reduce(javaReducer), is("((F && X) || T)"));
    azzert.that(F.and("X1", X).or(T).reduce(javaReducer), is("((F && X1) || T)"));
    azzert.that(F.or("X1", X).or(T).reduce(javaReducer), is("(F || X1 || T)"));
  }
  @Test public void b2a() {
    azzert.that(OR(F, X, N).and(T).reduce(javaReducer), is("((F || X || N) && T)"));
  }
  @Test public void b3() {
    assert !hasCycles(T);
    assert !hasCycles(X);
    assert !hasCycles(F);
    assert !hasCycles(N);
    assert !hasCycles(T.and(F));
    assert !hasCycles(F.and(T));
    assert !hasCycles(T.and(T));
    assert !hasCycles(T.or(T));
    assert !hasCycles(T_AND_F_AND_X);
    assert !hasCycles(T_OR_F_OR_X);
  }
  @Test public void b31() {
    assert !hasCycles(T.or(T));
  }
  @Test public void b32() {
    assert !hasCycles(T.and(T));
  }
  @Test public void b4() {
    T.reduce(javaReducer);
  }
  @Test public void b5() {
    F.and(F).reduce(javaReducer);
  }
  @Test public void b8() {
    T.or(X).reduce(javaReducer);
  }
  @Test public void b9() {
    azzert.that(X.reduce(javaReducer), instanceOf(String.class));
    azzert.that(X.reduce(javaReducer), is("X"));
    azzert.that(T.reduce(javaReducer), is("T"));
    azzert.that(T.or(X).reduce(javaReducer), is("(T || X)"));
  }
  @Test public void b91() {
    azzert.that(T.and(X).reduce(javaReducer), is("(T && X)"));
  }
  @Test public void b92() {
    azzert.that(not(T).reduce(javaReducer), is("!T"));
  }
  @Test public void b93() {
    azzert.that(not(X).reduce(javaReducer), is("!X"));
  }
  @Test public void b94() {
    azzert.that(not(N).reduce(javaReducer), is("!N"));
  }
  @Test public void b95() {
    azzert.that(that(T).reduce(javaReducer), is("T"));
  }
  @Test public void d() {
    azzert.that(T_OR_F_OR_X.reduce(javaReducer), is("(T || F || X)"));
  }
  @Test public void d0() {
    T.or(X, X).reduce(javaReducer);
    azzert.that(T.or(X, X).reduce(javaReducer), is("(T || X || X)"));
  }
  @Test public void d1() {
    azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
      @Override protected String map(final BooleanSupplier ¢) {
        return ¢ + "";
      }
    }), is("TFX"));
  }
  @Test public void e() {
    azzert.that(T_AND_F_AND_X.reduce(javaReducer), is("(T && F && X)"));
  }
  @Test public void f() {
    azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer), is("((!F && !F) || X || N || T)"));
  }
  @Test public void f0() {
    azzert.that(T + "", is("T"));
    azzert.that(F + "", is("F"));
    azzert.that(N + "", is("N"));
    azzert.that(X + "", is("X"));
  }
  @Test public void g() {
    azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer), is("(T || F || X || (!F && !F) || X || N || T)"));
  }
  @Test public void g1() {
    azzert.that(Proposition.not(F).and(X).reduce(javaReducer), is("(!F && X)"));
    azzert.that(Proposition.not(F).or(X).reduce(javaReducer), is("(!F || X)"));
  }
  @Test public void g2() {
    azzert.that(Proposition.that(F).and(X).reduce(javaReducer), is("(F && X)"));
    azzert.that(Proposition.that(F).or(X).reduce(javaReducer), is("(F || X)"));
  }
  @Test public void j() {
    azzert.that(B3.reduce(javaReducer), is("((!F && !F) || X || N || T)"));
    azzert.that(NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer), is("((!F && !F) || X || N || T)"));
    azzert.that(T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T.reduce(javaReducer), is("(T || F || X || (!F && !F) || X || N || T)"));
    azzert.that(B4.reduce(javaReducer), is("(T || F || X || (!F && !F) || X || N || T)"));
    azzert.that(B5.reduce(javaReducer), is("((T || F || X) && T && F && X)"));
    azzert.that(B1_AND_B2.reduce(javaReducer), is("((T || F || X) && T && F && X)"));
    azzert.that(B6.reduce(javaReducer), is("(T && F && X && (T || F || X))"));
    azzert.that(B2_AND_B1.reduce(javaReducer), is("(T && F && X && (T || F || X))"));
    azzert.that(B7.reduce(javaReducer), is("(T || F || X || (T && F && X))"));
    azzert.that(B1_OR_B2.reduce(javaReducer), is("(T || F || X || (T && F && X))"));
    azzert.that(B8.reduce(javaReducer), is("((T && F && X) || T || F || X)"));
    azzert.that(B2_OR_B1.reduce(javaReducer), is("((T && F && X) || T || F || X)"));
  }
  @Before public void setUp() {
    B1 = T_OR_F_OR_X = Proposition.OR("T OR F OR X", T, F, X);
    B2 = T_AND_F_AND_X = Proposition.AND("T AND F AND X", T, F, X);
    B3 = NOT_F_AND_NOT_F_OR_X_OR_N_OR_T = not(F).and(not(F)).or(X).or(N, T);
    B4 = T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T = Proposition.OR(T_OR_F_OR_X, NOT_F_AND_NOT_F_OR_X_OR_N_OR_T);
    B5 = B1_AND_B2 = B1.and(B2);
    B6 = B2_AND_B1 = B2.and(B1);
    B7 = B1_OR_B2 = B1.or(B2);
    B8 = B2_OR_B1 = B2.or(B1);
  }
  @Test public void z() {
    azzert.that(T_OR_F_OR_X.reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
      @Override protected String map(@SuppressWarnings("unused") final BooleanSupplier __) {
        return "";
      }
    }), is(""));
  }
  @Test public void z1() {
    assert T_OR_F_OR_X.eval();
  }
  private boolean hasCycles(final BooleanSupplier s) {
    final Stack<BooleanSupplier> path = new Stack<>();
    path.add(s);
    final Queue<BooleanSupplier> todo = new LinkedList<>();
    do {
      final BooleanSupplier current = todo.isEmpty() ? path.pop() : todo.remove();
      if (path.contains(current))
        return true;
      if (current instanceof Proposition.Some) {
        todo.addAll(((Proposition.Some) current).inner);
        continue;
      }
      if (current instanceof Proposition.Singleton)
        path.push(((Proposition.Singleton) current).inner);
    } while (!path.isEmpty());
    return false;
  }

  private Proposition B1, T_OR_F_OR_X;
  private Proposition B2, T_AND_F_AND_X;
  private Proposition B3, NOT_F_AND_NOT_F_OR_X_OR_N_OR_T;
  private Proposition B4, T_OR_F_OR_X_OR_NOT_F_AND_NOT_F_OR_X_OR_N_OR_T;
  private Proposition B5, B1_AND_B2;
  private Proposition B6, B2_AND_B1;
  private Proposition B7, B1_OR_B2;
  private Proposition B8, B2_OR_B1;
  private final PropositionJavaNotation javaReducer = new PropositionJavaNotation();
  Proposition condition;
  Proposition inner;
  Object object;
  BooleanSupplier supplier;
}
