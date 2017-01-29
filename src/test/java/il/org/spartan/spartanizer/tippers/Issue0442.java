package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;

/** Tests of {@link SingelVariableDeclarationUnderscoreDoubled}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0442 {
  public static class WorkInProgress {
    @Test public void b() {
      trimmingOf("public S f(X x){return null;}")//
          .gives("public S f(X __){return null;}")//
          .stays();
    }
  }

  @Test public void a() {
    trimmingOf("public abstract S f( X x);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("public S f(X x){return null;}")//
        .gives("public S f(X __){return null;}")//
        .stays();
  }

  @Test public void c$etc() {
    trimmingOf("interface I{ I f(I i);}")//
        .stays();
  }

  @Test public void chocolate01() {
    assert true;
  }

  @Test public void chocolate02() {
    assert true;
  }

  @Test public void chocolate03etc() {
    assert true;
  }

  @Test public void demoOfAzzert() {
    azzert.that(guessName.of("__"), is(guessName.ANONYMOUS));
    azzert.that(precedence.of(e("a+b")), is(5));
    azzert.that(namer.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
    azzert.that(minus.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
    azzert.that(metrics.literals(i("3+4+5+6")), hasItem("6"));
  }

  @Test public void vanilla01() {
    assert true;
  }

  @Test public void vanilla02() {
    assert true;
  }

  @Test public void vanilla03etc() {
    assert true;
  }
}
