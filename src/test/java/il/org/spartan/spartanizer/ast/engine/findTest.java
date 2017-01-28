/** TODO: orimarco <marcovitch.ori@gmail.com> please add a description
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Dec 22, 2016 */
package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.navigate.find.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class findTest {
  @Test public void a() {
    azzert.that(singleAtomicDifference(ast("a"), ast("b")), is("a"));
  }

  @Test public void b() {
    azzert.that(singleAtomicDifference(ast("a+b"), ast("b+b")), is("a"));
  }

  @Test public void c() {
    azzert.that(singleAtomicDifference(ast("f(a+b,a)"), ast("f(c+b,c)")), is("a"));
  }

  @Test public void d() {
    azzert.that(singleAtomicDifference(ast("f(a+b,a(g))"), ast("f(c+b,c(g))")), is("a"));
  }

  @Test public void f() {
    azzert.isNull(singleAtomicDifference(ast("f(a+b,d)"), ast("f(c+b,c)")));
  }

  @Test public void g() {
    azzert.that(singleAtomicDifference(ast("f(a+b,c)"), ast("f(a+b,c)")), is(""));
  }

  @Test public void h() {
    azzert.that(singleAtomicDifference(ast("bools(a == 0)"), ast("bools(a == 1)")), is("0"));
  }

  @Test public void i() {
    azzert.that(singleAtomicDifference(as.list(ast("bools(a == 0)"), ast("bools(a == 1)"), ast("bools(a == 2)"))), is("0"));
  }

  @Test public void j() {
    azzert.isNull(singleAtomicDifference(as.list(ast("a + b"), ast("a * b"))));
  }

  @Test public void e0() {
    azzert.that(singleExpressionDifference(as.list(ast("a + b"), ast("a * b"))) + "", is("a + b"));
  }

  @Test public void e1() {
    azzert.isNull(singleExpressionDifference(as.list(ast("a + b"), ast("a + b"))));
  }

  @Test public void e2() {
    azzert.that(singleExpressionDifference(as.list(ast("x == a + b"), ast("y == a + b"))) + "", is("x"));
  }

  @Test public void e3() {
    azzert.that(singleExpressionDifference(as.list(ast("x(f,g + 1) == a + b"), ast("y == a + b"))) + "", is("x(f,g + 1)"));
  }

  @Test public void e4() {
    azzert.that(singleExpressionDifference(as.list(ast("x(f,g + 1) == a + b"), ast("y == x(f,g + 1) + b"))) + "", is("x(f,g + 1) == a + b"));
  }

  @Test public void e5() {
    azzert.that(singleExpressionDifference(as.list(ast("x(f,g + 1) * 6 > a + b"), ast("x(t,g2 + 1) * 6 == a + b"))) + "",
        is("x(f,g + 1) * 6 > a + b"));
  }

  @Test public void e6() {
    azzert.that(singleExpressionDifference(as.list(ast("x(f,g + 1) * 6 > a + b"), ast("x(t,g + 1) * 6 > a + b"))) + "", is("f"));
  }
}
