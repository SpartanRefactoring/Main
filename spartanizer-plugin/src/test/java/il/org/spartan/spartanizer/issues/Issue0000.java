package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.equalTo;
import static fluent.ly.azzert.hasItem;
import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.i;
import static il.org.spartan.spartanizer.engine.parse.t;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.engine.nominal.abbreviate;
import il.org.spartan.spartanizer.engine.nominal.guessName;
import il.org.spartan.spartanizer.java.precedence;

/** Tests of {@link ThisClass#thatFunction}
 * @author Yossi Gil
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0000 {
  @Test public void a() {
    assert true;
  }
  @Test public void b() {
    assert true;
  }
  @Test public void c$etc() {
    assert true;
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
    azzert.that(abbreviate.it(t("List<Set<Integer>> __;")), equalTo("iss"));
    azzert.that(compute.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
    azzert.that(Metrics.literals(i("3+4+5+6")), hasItem("6"));
  }
  /** Correct way of trimming does not change */
  @Test public void demoOfTrimming() {
    trimmingOf("a")//
        .stays();
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
