package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests of {@link enumerate.expressions}
 * @author Ori Marcovitch
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue552 {
  @Test public void a0() {
    enumerate.expressions(null);
  }

  @Test public void a() {
    auxInt(enumerate.expressions((ASTNode) null));
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

  static void auxInt(@SuppressWarnings("unused") final int __) {
    assert true;
  }

  static ASTNode ast(final String ¢) {
    return wizard.ast(¢);
  }
}
