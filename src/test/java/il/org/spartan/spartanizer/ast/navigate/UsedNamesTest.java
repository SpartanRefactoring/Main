package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.util.stream.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** tests {@link extract#usedNames(Expression)}
 * @author Yossi Gil
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class UsedNamesTest {
  @Test public void a() {
    azzert.that(compute.usedNames(into.e("0")).count(), is(0L));
  }

  @Test public void b() {
    azzert.that(compute.usedNames(into.e("a")).count(), is(1L));
  }

  @Test public void c() {
    azzert.that(compute.usedNames(into.e("a+0")).count(), is(1L));
  }

  @Test public void d() {
    azzert.that(compute.usedNames(into.e("f()")).count(), is(0L));
  }

  @Test public void e() {
    azzert.that(compute.usedNames(into.e("this.f(a)")).count(), is(1L));
  }

  @Test public void f() {
    final Stream<String> usedNames = compute.usedNames(into.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.count(), is(0L));
  }
}
