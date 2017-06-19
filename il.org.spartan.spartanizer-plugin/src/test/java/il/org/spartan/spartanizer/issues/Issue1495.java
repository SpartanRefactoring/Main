package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** TODO Yuval Simon: document class 
 * 
 * @author Yuval Simon
 * @since 2017-06-19 */
@SuppressWarnings("static-method")
public class Issue1495 { 
  @Test public void t1() {
    trimmingOf("boolean b2, b3 = ownerType == null;"
        + "b2 = !b3 ? ownerType != null : !ownerType.equals(ownerType + a);")
    .using(VariableDeclarationFragment.class, new LocalUninitializedAssignment())
    .stays();
  }
}
