package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

/** Tests of class {@link Int}
 * @author Yossi Gil
 * @year 2016 */
@SuppressWarnings("static-method") public class IntTest {
  @Test public void a() {
    Int.class.hashCode();
  }
  @Test public void b() {
    new Int().hashCode();
  }
  @Test public void c() {
    azzert.that(new Int().inner, is(0));
  }
  @Test public void d() {
    azzert.that(new Int(0).inner, is(0));
  }
  @Test public void e() {
    azzert.that(new Int(3).inner, is(3));
  }
  @Test public void f() {
    azzert.that(Int.of(3).inner(), is(3));
  }
}
