package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method") public class Issue390 {
  @Test public void demoOfAzzert() {
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
    azzert.that(precedence.of(e("a+b")), is(5));
    azzert.that(spartan.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
    azzert.that(minus.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
    azzert.that(metrics.literals(i("3+4+5+6")), hasItem("6"));
  }}
