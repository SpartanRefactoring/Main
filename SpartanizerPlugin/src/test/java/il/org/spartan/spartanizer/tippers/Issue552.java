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
    assertEquals(1, count.expressions(wizard.ast("return a;")));
  }

  void auxInt(@SuppressWarnings("unused") int __) {
    assert true;
  }
}
