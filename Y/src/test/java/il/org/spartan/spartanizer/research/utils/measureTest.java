package il.org.spartan.spartanizer.research.utils;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests {@link measure}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class measureTest {
  @Test public void a() {
    azzert.that(measure.commands(make.ast("public void foo(){}")), is(0));
  }

  @Test public void b() {
    azzert.that(measure.commands(make.ast("public void foo(){use();}")), is(1));
  }

  @Test public void c() {
    azzert.that(measure.commands(make.ast("public void foo(){int x;foo(x,x);}")), is(1));
  }
}
