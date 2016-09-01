package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;

@SuppressWarnings("static-method") public class RestructureTest {
  @Test public void issue72me4xA() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xB() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xC() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("+x")), iz("-x"));
  }

  @Test public void issue72me4xD() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xF() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xG() {
    azzert.that(il.org.spartan.refactoring.assemble.make.minus(into.e("+x")), iz("-x"));
  }
}