package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

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
    assertEquals(1, enumerate.expressions(ast("a")));
  }
  @Test public void c() {
    assertEquals(3, enumerate.expressions(ast("a + b")));
  }
  @Test public void d() {
    assertEquals(0, enumerate.expressions(null));
  }
  @Test public void e() {
    assertEquals(4, enumerate.expressions(ast("a + b + c")));
  }
  @Test public void f() {
    assertEquals(4, enumerate.expressions(ast("return a + b + c;")));
  }
  @Test public void g() {
    assertEquals(4, enumerate.expressions(ast("if(a == null) return null;")));
  }
  @Test public void h() {
    assertEquals(4, enumerate.expressions(ast("while(true) print(i);")));
  }
  @Test public void i() {
    assertEquals(1, enumerate.expressions(ast("true")));
  }
  @Test public void j() {
    assertEquals(3, enumerate.expressions(ast("1 + 2")));
  }
  static void auxInt(@SuppressWarnings("unused") final int __) {
    assert true;
  }
  static ASTNode ast(final String ¢) {
    return wizard.ast(¢);
  }
}
