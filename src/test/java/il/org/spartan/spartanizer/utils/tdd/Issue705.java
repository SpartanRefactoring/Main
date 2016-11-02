package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests of {@link enumerate.expressions}
 * @author Koby Ben Shimol
 * @author Yuval Simon
 * @since 16-11-2 */
public class Issue705 {
  @Test public void a() {
    auxList(getAll.instanceofs((MethodDeclaration) null));
  }
  void auxList(@SuppressWarnings("unused") List<InstanceofExpression> __) {
    assert true;
  }
}
