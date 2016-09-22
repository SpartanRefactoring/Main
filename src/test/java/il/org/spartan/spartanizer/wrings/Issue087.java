package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue087 {
  @Test public void a() {
    trimmingOf("a-b*c - (x - - - (d*e))").gives("a  - b*c -x + d*e");
  }

  @Test public void b() {
    trimmingOf("a-b*c").stays();
  }

  @Test public void c() {
    trimmingOf("a + (b-c)").stays();
  }

  @Test public void d() {
    trimmingOf("a - (b-c)").gives("a - b + c");
  }
}
