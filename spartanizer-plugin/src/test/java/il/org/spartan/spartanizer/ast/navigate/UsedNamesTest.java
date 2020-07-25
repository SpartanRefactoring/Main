package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.is;

import java.util.stream.Stream;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.engine.parse;

/** tests {@link extract#usedIdentifiers(Expression)}
 * @author Yossi Gil
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class UsedNamesTest {
  @Test public void a() {
    azzert.that(compute.usedIdentifiers(parse.e("0")).count(), is(0L));
  }
  @Test public void b() {
    azzert.that(compute.usedIdentifiers(parse.e("a")).count(), is(1L));
  }
  @Test public void c() {
    azzert.that(compute.usedIdentifiers(parse.e("a+0")).count(), is(1L));
  }
  @Test public void d() {
    azzert.that(compute.usedIdentifiers(parse.e("f()")).count(), is(0L));
  }
  @Test public void e() {
    azzert.that(compute.usedIdentifiers(parse.e("this.f(a)")).count(), is(1L));
  }
  @Test public void f() {
    final Stream<String> usedNames = compute.usedIdentifiers(parse.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.count(), is(0L));
  }
}
