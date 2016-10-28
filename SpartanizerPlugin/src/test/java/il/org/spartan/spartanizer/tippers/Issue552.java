package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Tests of {@link enumerate.expressions}
 * @author Ori Marcovitch
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue552 {
  @Test public void a() {
    auxInt(enumerate.expressions((ASTNode) null));
  }

  @Test public void b() {
    assertEquals(1, enumerate.expressions(wizard.ast("a")));
  }

  @Test public void c() {
    assertEquals(3, enumerate.expressions(wizard.ast("a + b")));
  }

  @Test public void d() {
    assertEquals(0, enumerate.expressions(null));
  }

  @Test public void e() {
    assertEquals(4, enumerate.expressions(wizard.ast("a + b + c")));
  }

  @Test public void f() {
    assertEquals(4, enumerate.expressions(wizard.ast("return a + b + c;")));
  }

  @Test public void g() {
    assertEquals(4, enumerate.expressions(wizard.ast("if(a == null) return null;")));
  }

  @Test public void h() {
    assertEquals(4, enumerate.expressions(wizard.ast("while(true) print(i);")));
  }

  @Test public void i() {
    assertEquals(1, enumerate.expressions(wizard.ast("true")));
  }

  @Test public void j() {
    assertEquals(3, enumerate.expressions(wizard.ast("1 + 2")));
  }

  void auxInt(@SuppressWarnings("unused") int __) {
    assert true;
  }
}
