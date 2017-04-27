package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.util.stream.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** tests {@link extract#usedIdentifiers(Expression)}
 * @author Yossi Gil
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class UsedNamesTest {
  @Test public void a() {
    azzert.that(compute.usedIdentifiers(into.e("0")).count(), is(0L));
  }

  @Test public void b() {
    azzert.that(compute.usedIdentifiers(into.e("a")).count(), is(1L));
  }

  @Test public void c() {
    azzert.that(compute.usedIdentifiers(into.e("a+0")).count(), is(1L));
  }

  @Test public void d() {
    azzert.that(compute.usedIdentifiers(into.e("f()")).count(), is(0L));
  }

  @Test public void e() {
    azzert.that(compute.usedIdentifiers(into.e("this.f(a)")).count(), is(1L));
  }

  @Test public void f() {
    final Stream<String> usedNames = compute.usedIdentifiers(into.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.count(), is(0L));
  }
}
