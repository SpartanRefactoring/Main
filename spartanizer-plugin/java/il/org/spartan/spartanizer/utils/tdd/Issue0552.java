package il.org.spartan.spartanizer.utils.tdd;

import static fluent.ly.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.research.util.*;

/** Tests of {@link measure.expressions}
 * @author Ori Marcovitch
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0552 {
  static ASTNode ast(final String ¢) {
    return make.ast(¢);
  }
  static void auxInt(@SuppressWarnings("unused") final int __) {
    assert true;
  }
  @Test public void a() {
    auxInt(enumerate.expressions(null));
  }
  @Test public void a0() {
    enumerate.expressions(null);
  }
  @Test public void b() {
    azzert.that(enumerate.expressions(ast("a")), is(1));
  }
  @Test public void c() {
    azzert.that(enumerate.expressions(ast("a + b")), is(3));
  }
  @Test public void d() {
    azzert.that(enumerate.expressions(null), is(0));
  }
  @Test public void e() {
    azzert.that(enumerate.expressions(ast("a + b + c")), is(4));
  }
  @Test public void f() {
    azzert.that(enumerate.expressions(ast("return a + b + c;")), is(4));
  }
  @Test public void g() {
    azzert.that(enumerate.expressions(ast("if(a == null) return null;")), is(4));
  }
  @Test public void h() {
    azzert.that(enumerate.expressions(ast("while(true) print(i);")), is(4));
  }
  @Test public void i() {
    azzert.that(enumerate.expressions(ast("true")), is(1));
  }
  @Test public void j() {
    azzert.that(enumerate.expressions(ast("1 + 2")), is(3));
  }
}
