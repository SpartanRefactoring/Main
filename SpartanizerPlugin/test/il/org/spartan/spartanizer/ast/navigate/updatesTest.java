package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;

/** Yossi Gil: tests {@link compute#updatedVariables }
 * @author Yossi Gil
 * @since 2017-03-05 */
@SuppressWarnings("static-method")
public class updatesTest {
  @Test public void a() {
    assert compute.updateSpots(into.e("i")) != null;
  }

  @Test public void b() {
    assert compute.updateSpots(into.e("i++")) != null;
  }

  @Test public void c() {
    azzert.that(compute.updateSpots(into.e("i++")).size(), is(1));
  }

  @Test public void d() {
    azzert.that(compute.updateSpots(into.e("++i")).size(), is(1));
  }

  @Test public void e() {
    azzert.that(compute.updateSpots(into.e("i=1")).size(), is(1));
  }

  @Test public void f() {
    azzert.that(compute.updateSpots(into.e("i=j=1")).size(), is(2));
  }

  @Test public void g() {
    azzert.that(compute.updateSpots(into.e("i=j=k++")).size(), is(3));
  }

  @Test public void h() {
    azzert.that(compute.updateSpots(into.e("-i")).size(), is(0));
  }
}
