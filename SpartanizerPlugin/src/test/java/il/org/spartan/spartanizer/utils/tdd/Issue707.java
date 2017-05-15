package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test class for {@link getAll2}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-04-08 */
@SuppressWarnings("static-method")
public class Issue707 {
  @Test public void a() {
    Assert.assertNull(getAll2.names(null));
  }
  @Test public void b() {
    assertEquals(getAll2.names(az.block(wizard.ast("{int i;}"))).get(0) + "", "i");
  }
  @Test public void c() {
    assertEquals(getAll2.names(az.block(wizard.ast("{int i = x;}"))).get(0) + "", "i");
    assertEquals(getAll2.names(az.block(wizard.ast("{int i = x;}"))).get(1) + "", "x");
  }
}
