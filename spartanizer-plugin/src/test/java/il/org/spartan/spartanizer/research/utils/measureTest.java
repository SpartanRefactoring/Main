package il.org.spartan.spartanizer.research.utils;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.research.util.measure;

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
