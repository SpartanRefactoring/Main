package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #714 for more details
 * @author Dan Abramovich
 * @author Assaf Lustig
 * @author Arthur Sapozhnikov
 * @since 16-11-02 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue714 {
  @Test public void testRetTypeCompiles() {
    @SuppressWarnings("unused") final boolean b = determineIf.isImmutable(null);
  }

  @Test public void testNull() {
    auxBool(determineIf.isImmutable((TypeDeclaration) null));
  }

  @Test public void testSimpleTypeDecleration() {
    assertTrue(determineIf.isImmutable((TypeDeclaration) az.compilationUnit(wizard.ast("public class A {}")).types().get(0)));
  }

  static void auxBool(@SuppressWarnings("unused") final boolean __) {
    assert true;
  }
}
