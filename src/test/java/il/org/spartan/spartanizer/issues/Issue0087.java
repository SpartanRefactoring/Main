package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0087 {
  @Test public void a() {
    trimmingOf("a-b*c - (x - - - (d*e))")//
        .gives("a  - b*c -x + d*e");
  }

  @Test public void b() {
    trimmingOf("a-b*c")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("a + (b-c)")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("a - (b-c)")//
        .gives("a - b + c");
  }
}
