package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** tests {@link extract#usedNames(Expression)}
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-09 */
@SuppressWarnings("static-method")
public class UsedNamesTest {
  @Test public void a() {
    azzert.that(extract.usedNames(into.e("0")).size(), is(0));
  }
  @Test public void b() {
    azzert.that(extract.usedNames(into.e("a")).size(), is(1));
  }
  @Test public void c() {
    azzert.that(extract.usedNames(into.e("a+0")).size(), is(1));
  }
  @Test public void d() {
    azzert.that(extract.usedNames(into.e("f()")).size(), is(0));
  }
  @Test public void e() {
    azzert.that(extract.usedNames(into.e("this.f(a)")).size(), is(1));
  }
  @Test public void f() {
    final List<String> usedNames = extract.usedNames(into.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.size(), is(0));
  }
}
