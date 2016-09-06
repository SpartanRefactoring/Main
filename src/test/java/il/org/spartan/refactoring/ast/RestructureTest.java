package il.org.spartan.refactoring.ast;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.engine.*;

@SuppressWarnings("static-method") //
public class RestructureTest {
  @Test public void issue72me4xA() {
    azzert.that(make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xB() {
    azzert.that(make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xC() {
    azzert.that(make.minus(into.e("+x")), iz("-x"));
  }

  @Test public void issue72me4xD() {
    azzert.that(make.minus(into.e("-x")), iz("x"));
  }

  @Test public void issue72me4xF() {
    azzert.that(make.minus(into.e("x")), iz("-x"));
  }

  @Test public void issue72me4xG() {
    azzert.that(make.minus(into.e("+x")), iz("-x"));
  }
}