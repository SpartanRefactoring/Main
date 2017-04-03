package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** tests {@link extract#useSpots(Expression)}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class UsedNamesTest {
  @Test public void a() {
    azzert.that(compute.useSpots(into.e("0")).size(), is(0));
  }

  @Test public void b() {
    azzert.that(compute.useSpots(into.e("a")).size(), is(1));
  }

  @Test public void c() {
    azzert.that(compute.useSpots(into.e("a+0")).size(), is(1));
  }

  @Test public void d() {
    azzert.that(compute.useSpots(into.e("f()")).size(), is(0));
  }

  @Test public void e() {
    azzert.that(compute.useSpots(into.e("this.f(a)")).size(), is(1));
  }

  @Test public void f() {
    final List<String> usedNames = compute.useSpots(into.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.size(), is(0));
  }
}
