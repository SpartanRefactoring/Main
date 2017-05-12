package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.ast.navigate.find.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;

/** TODO orimarco <marcovitch.ori@gmail.com> please add a description
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Dec 22, 2016 */
@SuppressWarnings({ "javadoc", "static-method" })
public final class findTest {
  @Test public void a() {
    azzert.that(singleAtomicDifference(make.ast("a"), make.ast("b")), is("a"));
  }

  @Test public void b() {
    azzert.that(singleAtomicDifference(make.ast("a+b"), make.ast("b+b")), is("a"));
  }

  @Test public void c() {
    azzert.that(singleAtomicDifference(make.ast("f(a+b,a)"), make.ast("f(c+b,c)")), is("a"));
  }

  @Test public void d() {
    azzert.that(singleAtomicDifference(make.ast("f(a+b,a(g))"), make.ast("f(c+b,c(g))")), is("a"));
  }

  @Test public void e0() {
    azzert.that(singleExpressionDifference(as.list(make.ast("a + b"), make.ast("a * b"))) + "", is("a + b"));
  }

  @Test public void e1() {
    azzert.isNull(singleExpressionDifference(as.list(make.ast("a + b"), make.ast("a + b"))));
  }

  @Test public void e2() {
    azzert.that(singleExpressionDifference(as.list(make.ast("x == a + b"), make.ast("y == a + b"))) + "", is("x"));
  }

  @Test public void e3() {
    azzert.that(singleExpressionDifference(as.list(make.ast("x(f,g + 1) == a + b"), make.ast("y == a + b"))) + "", is("x(f,g + 1)"));
  }

  @Test public void e4() {
    azzert.that(singleExpressionDifference(as.list(make.ast("x(f,g + 1) == a + b"), make.ast("y == x(f,g + 1) + b"))) + "",
        is("x(f,g + 1) == a + b"));
  }

  @Test public void e5() {
    azzert.that(singleExpressionDifference(as.list(make.ast("x(f,g + 1) * 6 > a + b"), make.ast("x(t,g2 + 1) * 6 == a + b"))) + "",
        is("x(f,g + 1) * 6 > a + b"));
  }

  @Test public void e6() {
    azzert.that(singleExpressionDifference(as.list(make.ast("x(f,g + 1) * 6 > a + b"), make.ast("x(t,g + 1) * 6 > a + b"))) + "", is("f"));
  }

  @Test public void f() {
    azzert.isNull(singleAtomicDifference(make.ast("f(a+b,d)"), make.ast("f(c+b,c)")));
  }

  @Test public void g() {
    azzert.that(singleAtomicDifference(make.ast("f(a+b,c)"), make.ast("f(a+b,c)")), is(""));
  }

  @Test public void h() {
    azzert.that(singleAtomicDifference(make.ast("bools(a == 0)"), make.ast("bools(a == 1)")), is("0"));
  }

  @Test public void i() {
    azzert.that(singleAtomicDifference(as.list(make.ast("bools(a == 0)"), make.ast("bools(a == 1)"), make.ast("bools(a == 2)"))), is("0"));
  }

  @Test public void j() {
    azzert.isNull(singleAtomicDifference(as.list(make.ast("a + b"), make.ast("a * b"))));
  }
}
