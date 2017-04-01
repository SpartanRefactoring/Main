package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Proposition.*;

import java.util.function.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.proposition.*;
import il.org.spartan.utils.*;

/**
 * Proposition auto-simplifies tests 
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-01
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1200 {
  
  private final PropositionJavaNotation javaReducer = new PropositionJavaNotation();

  @Test @SuppressWarnings("unused") public void a() {
    azzert.that(OR(T,OR(T,F)).reduce(javaReducer), is("(T || T || F)"));
  }

  @Test @SuppressWarnings("unused") public void b() {
    azzert.that(OR(T,of("T || F",OR(T,F))).reduce(javaReducer), is("(T || (T || F))"));
  }
  
  @Test @SuppressWarnings("unused") public void c() {
    azzert.that(F.or("X1", X).or(T).reduce(javaReducer), is("(F || X1 || T)"));
  }
  
  @Test @SuppressWarnings("unused") public void d() {
    azzert.that(F.and("X1", X).and(T).reduce(javaReducer), is("(F && X1 && T)"));
  }
  
  @Test @SuppressWarnings("unused") public void e() {
    azzert.that(Proposition.OR("T OR F OR X", T, F, X).and(Proposition.AND("T AND F AND X", T, F, X)).reduce(javaReducer), is("((T || F || X) && T && F && X)"));
  }
}
