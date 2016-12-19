package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.spartanizer.engine.into.*;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc", "static-method" })
public final class wizardTest {
  @Test public void astExpression() {
    assert iz.expression(wizard.ast("x + y"));
  }

  @Test public void sameOfNullAndSomething() {
    assert !same(null, e("a"));
  }

  @Test public void sameOfNulls() {
    assert same((ASTNode) null, (ASTNode) null);
  }

  @Test public void sameOfSomethingAndNull() {
    assert !same(e("a"), (Expression) null);
  }

  @Test public void sameOfTwoExpressionsIdentical() {
    assert same(e("a+b"), e("a+b"));
  }

  @Test public void sameOfTwoExpressionsNotSame() {
    assert !same(e("a+b+c"), e("a+b"));
  }

  @Test public void a() {
    assertEquals("a", findSingleAtomicDifference(ast("a"), ast("b")));
  }

  @Test public void b() {
    assertEquals("a", findSingleAtomicDifference(ast("a+b"), ast("b+b")));
  }

  @Test public void c() {
    assertEquals("a", findSingleAtomicDifference(ast("f(a+b,a)"), ast("f(c+b,c)")));
  }

  @Test public void d() {
    assertEquals("a", findSingleAtomicDifference(ast("f(a+b,a(g))"), ast("f(c+b,c(g))")));
  }

  @Test public void f() {
    assertEquals(null, findSingleAtomicDifference(ast("f(a+b,d)"), ast("f(c+b,c)")));
  }

  @Test public void g() {
    assertEquals("", findSingleAtomicDifference(ast("f(a+b,c)"), ast("f(a+b,c)")));
  }

  @Test public void h() {
    assertEquals("0", findSingleAtomicDifference(ast("bools(a == 0)"), ast("bools(a == 1)")));
  }

  @Test public void i() {
    assertEquals("0", findSingleAtomicDifference(Arrays.asList(ast("bools(a == 0)"), ast("bools(a == 1)"), ast("bools(a == 2)"))));
  }
}
