package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** unit tests for {@link InfixStringLiteralsConcatenate}
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
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
    trimmingOf("String x = \"a\"+\"b\"+f()+\"c\"+\n\"d\"+5;")//
        .gives("String x = \"ab\"+f()+\"c\"+\n\"d\"+5;")//
        .stays();
  }
}
