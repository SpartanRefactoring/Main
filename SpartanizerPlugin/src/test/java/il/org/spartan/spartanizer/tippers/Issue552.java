package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Tests of {@link count.expressions}
 * @author Ori Marcovitch
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue552 {
  @Test public void a() {
    auxInt(count.expressions((ASTNode) null));
  }

  @Test public void b() {
    assertEquals(1, count.expressions(wizard.ast("a")));
  }

  @Test public void c() {
    assertEquals(2, count.expressions(wizard.ast("a + b")));
  }

  @Test public void d() {
    assertEquals(0, count.expressions(null));
  }

  @Test public void e() {
    assertEquals(3, count.expressions(wizard.ast("a + b + c")));
  }

  @Test public void f() {
    assertEquals(3, count.expressions(wizard.ast("return a + b + c;")));
  }

//  @Test public void g() {
//    assertEquals(3, count.expressions(wizard.ast("if(a == null) return null;")));
//  }
//
//  @Test public void h() {
//    assertEquals(3, count.expressions(wizard.ast("while(true) print(i);")));
//  }

  void auxInt(@SuppressWarnings("unused") int __) {
    assert true;
  }
}
