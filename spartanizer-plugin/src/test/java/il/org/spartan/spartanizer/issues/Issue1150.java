package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.tippers.InfixStringLiteralsConcatenate;

/** unit tests for {@link InfixStringLiteralsConcatenate}
 * @author Doron Mehsulam {@code doronmmm@hotmail.com}
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue1150 {
  @Test public void a() {
    trimmingOf("\"abs\"+\"de\"")//
        .gives("\"absde\"")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("\"abs\"+3+\"de\"")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("\"abs\"+\"de\"+\"de\"")//
        .gives("\"absdede\"")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("\"a\"+\"b\"+3+\"c\"+\"d\"")//
        .gives("\"a\"+\"b\"+3+\"cd\"")//
        .gives("\"ab\"+3+\"cd\"")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("\"a\"+\n\"b\"")//
        .stays();
  }
  @Test public void f() {
    trimmingOf("String x = \"a\"+\"b\"+f()+\"c\"+\n\"d\"+5;f(x); g(x);")//
        .gives("String x = \"ab\"+f()+\"c\"+\n\"d\"+5;f(x); g(x);")//
        .stays();
  }
}
