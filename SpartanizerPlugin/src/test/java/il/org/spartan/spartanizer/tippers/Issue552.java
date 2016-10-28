package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;
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

  void auxInt(@SuppressWarnings("unused") int __) {
    assert true;
  }
}
