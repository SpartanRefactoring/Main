package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.engine.parse;

/** Yossi Gil: tests {@link compute#updatedVariables }
 * @author Yossi Gil
 * @since 2017-03-05 */
@SuppressWarnings("static-method")
public class updatesTest {
  @Test public void a() {
    assert compute.updateSpots(parse.e("i")) != null;
  }
  @Test public void b() {
    assert compute.updateSpots(parse.e("i++")) != null;
  }
  @Test public void c() {
    azzert.that(compute.updateSpots(parse.e("i++")).size(), is(1));
  }
  @Test public void d() {
    azzert.that(compute.updateSpots(parse.e("++i")).size(), is(1));
  }
  @Test public void e() {
    azzert.that(compute.updateSpots(parse.e("i=1")).size(), is(1));
  }
  @Test public void f() {
    azzert.that(compute.updateSpots(parse.e("i=j=1")).size(), is(2));
  }
  @Test public void g() {
    azzert.that(compute.updateSpots(parse.e("i=j=k++")).size(), is(3));
  }
  @Test public void h() {
    azzert.that(compute.updateSpots(parse.e("-i")).size(), is(0));
  }
}
