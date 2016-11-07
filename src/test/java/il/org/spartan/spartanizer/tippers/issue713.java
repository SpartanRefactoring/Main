package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** see issue #713 for more details
 * @author Inbal Zukerman
 * @author Elia Traore
 * @since 2016-11-06 */
public class issue713 {
  TypeDeclaration noPublic = createTypeDeclerationFromStr("public class noPublic {   } ");
  TypeDeclaration onePublic = createTypeDeclerationFromStr("public class onePublic {  public int x; } ");

  @SuppressWarnings("static-method") @Test public void doesCompile() {
    assert true;
  }

  @SuppressWarnings("static-method") @Test public void returnsList() {
    @SuppressWarnings("unused") final List<String> lst = getAll.publicFields(null);
  }

  @Test public void returnsNoPublic() {
    assertEquals(0, getAll.publicFields(noPublic).size());
  }

  @Test public void onePublicFails() {
    assertNotEquals(1, getAll.publicFields(onePublic).size());
  }

  private static TypeDeclaration createTypeDeclerationFromStr(final String ¢) {
    return az.typeDeclaration(wizard.ast(¢));
  }
}
