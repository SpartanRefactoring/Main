package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class RestructureTest {
  @Test public void issue72me4xA() {
    azzert.that(Restructure.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xB() {
    azzert.that(Restructure.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xC() {
    azzert.that(Restructure.minus(into.e("+x")), iz("-x"));
  }

  @Test public void issue72me4xD() {
    azzert.that(Restructure.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xF() {
    azzert.that(Restructure.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xG() {
    azzert.that(Restructure.minus(into.e("+x")), iz("-x"));
  }
}