package il.org.spartan.spartanizer.research.utils;

import static il.org.spartan.azzert.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests {@link measure}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class measureTest {
  @Test public void a() {
    azzert.that(measure.commands(ast("public void foo(){}")), is(0));
  }

  @Test public void b() {
    azzert.that(measure.commands(ast("public void foo(){use();}")), is(1));
  }

  @Test public void c() {
    azzert.that(measure.commands(ast("public void foo(){int x;foo(x,x);}")), is(1));
  }
}
