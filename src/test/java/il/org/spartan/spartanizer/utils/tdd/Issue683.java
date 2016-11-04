package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;

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
}
