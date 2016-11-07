package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Tests of {@link tdd.find}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 4, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue683 {
  @Test public void a() {
    find.ancestorType(null);
  }

  @Test public void b() {
    auxTypeDeclaration(find.ancestorType((ASTNode) null));
  }

  static void auxTypeDeclaration(@SuppressWarnings("unused") final TypeDeclaration __) {
    assert true;
  }
}
