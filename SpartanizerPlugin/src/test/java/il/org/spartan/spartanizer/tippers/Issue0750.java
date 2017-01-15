package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @since 06-Dec-16
 * @author Doron Meshulam */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0750 {
  @Test public void testDidntSpoiledAnything() {
    trimmingOf("void f (int x) {for(Object o : x) { System.out.println(o);}}")//
        .gives("void f (int x) {for(Object ¢ : x) { System.out.println(¢);}}");
  }

  @Test public void testNoChange() {
    trimmingOf("void f (int ¢) {for(Object o : ¢) System.out.println(o);}")//
        .stays();
  }
}
