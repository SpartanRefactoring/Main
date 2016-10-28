package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.count;

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

  void auxInt(@SuppressWarnings("unused") final int __) {
    assert true;
  }
}
