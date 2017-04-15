package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** unit tests for {@link InfixStringLiteralsConcatenate}
 * @author Doron Mehsulam {@code doronmmm@hotmail.com}
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue1150 {
  @Test public void a() {
    trimminKof("\"abs\"+\"de\"")//
        .gives("\"absde\"")//
        .stays();
  }

  @Test public void b() {
    trimminKof("\"abs\"+3+\"de\"")//
        .stays();
  }

  @Test public void c() {
    trimminKof("\"abs\"+\"de\"+\"de\"")//
        .gives("\"absdede\"")//
        .stays();
  }

  @Test public void d() {
    trimminKof("\"a\"+\"b\"+3+\"c\"+\"d\"")//
        .gives("\"a\"+\"b\"+3+\"cd\"")//
        .gives("\"ab\"+3+\"cd\"")//
        .stays();
  }

  @Test public void e() {
    trimminKof("\"a\"+\n\"b\"")//
        .stays();
  }

  @Test public void f() {
    trimminKof("String x = \"a\"+\"b\"+f()+\"c\"+\n\"d\"+5;f(x); g(x);")//
        .gives("String x = \"ab\"+f()+\"c\"+\n\"d\"+5;f(x); g(x);")//
        .stays();
  }
}
