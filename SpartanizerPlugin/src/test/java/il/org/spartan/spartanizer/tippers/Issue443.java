package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue443 {
  @Test public void a() {
    trimmingOf("public void f (String[] ss) {}")//
        .gives("public void f (String[] __) {}").stays();
  }
  @Test public void b() {
    trimmingOf("public void f (String... ss) {}")//
        .gives("public void f (String... __) {}").stays();
  }
}
