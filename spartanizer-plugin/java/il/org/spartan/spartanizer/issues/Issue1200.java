package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static il.org.spartan.utils.Proposition.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.proposition.*;
import il.org.spartan.utils.*;

/** Proposition auto-simplifies tests
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-01 */
@SuppressWarnings("javadoc")
public class Issue1200 {
  private final PropositionJavaNotation javaReducer = new PropositionJavaNotation();

  @Test public void a() {
    azzert.that(OR(T, OR(T, F)).reduce(javaReducer), is("(T || T || F)"));
  }
  @Test public void b() {
    azzert.that(OR(T, Proposition.that("T || F", OR(T, F))).reduce(javaReducer), is("(T || (T || F))"));
  }
  @Test public void c() {
    azzert.that(F.or("X1", X).or(T).reduce(javaReducer), is("(F || X1 || T)"));
  }
  @Test public void d() {
    azzert.that(F.and("X1", X).and(T).reduce(javaReducer), is("(F && X1 && T)"));
  }
  @Test public void e() {
    azzert.that(Proposition.OR("T OR F OR X", T, F, X).and(Proposition.AND("T AND F AND X", T, F, X)).reduce(javaReducer),
        is("((T || F || X) && T && F && X)"));
  }
}
