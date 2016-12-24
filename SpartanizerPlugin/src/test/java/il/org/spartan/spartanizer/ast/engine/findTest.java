package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.ast.navigate.find.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class findTest {
  @Test public void a() {
    assertEquals("a", singleAtomicDifference(ast("a"), ast("b")));
  }

  @Test public void b() {
    assertEquals("a", singleAtomicDifference(ast("a+b"), ast("b+b")));
  }

  @Test public void c() {
    assertEquals("a", singleAtomicDifference(ast("f(a+b,a)"), ast("f(c+b,c)")));
  }

  @Test public void d() {
    assertEquals("a", singleAtomicDifference(ast("f(a+b,a(g))"), ast("f(c+b,c(g))")));
  }

  @Test public void f() {
    assertEquals(null, singleAtomicDifference(ast("f(a+b,d)"), ast("f(c+b,c)")));
  }

  @Test public void g() {
    assertEquals("", singleAtomicDifference(ast("f(a+b,c)"), ast("f(a+b,c)")));
  }

  @Test public void h() {
    assertEquals("0", singleAtomicDifference(ast("bools(a == 0)"), ast("bools(a == 1)")));
  }

  @Test public void i() {
    assertEquals("0", singleAtomicDifference(Arrays.asList(ast("bools(a == 0)"), ast("bools(a == 1)"), ast("bools(a == 2)"))));
  }

  @Test public void j() {
    assertEquals(null, singleAtomicDifference(Arrays.asList(ast("a + b"), ast("a * b"))));
  }

  @Test public void e0() {
    assertEquals("a + b", singleExpressionDifference(Arrays.asList(ast("a + b"), ast("a * b"))) + "");
  }

  @Test public void e1() {
    assertEquals(null, singleExpressionDifference(Arrays.asList(ast("a + b"), ast("a + b"))));
  }

  @Test public void e2() {
    assertEquals("x", singleExpressionDifference(Arrays.asList(ast("x == a + b"), ast("y == a + b"))) + "");
  }

  @Test public void e3() {
    assertEquals("x(f,g + 1)", singleExpressionDifference(Arrays.asList(ast("x(f,g + 1) == a + b"), ast("y == a + b"))) + "");
  }

  @Test public void e4() {
    assertEquals("x(f,g + 1) == a + b", singleExpressionDifference(Arrays.asList(ast("x(f,g + 1) == a + b"), ast("y == x(f,g + 1) + b"))) + "");
  }

  @Test public void e5() {
    assertEquals("x(f,g + 1) * 6 > a + b",
        singleExpressionDifference(Arrays.asList(ast("x(f,g + 1) * 6 > a + b"), ast("x(t,g2 + 1) * 6 == a + b"))) + "");
  }

  @Test public void e6() {
    assertEquals("f", singleExpressionDifference(Arrays.asList(ast("x(f,g + 1) * 6 > a + b"), ast("x(t,g + 1) * 6 > a + b"))) + "");
  }
}
