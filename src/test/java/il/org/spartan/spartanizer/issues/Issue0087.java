package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0087 {
  @Test public void a() {
    trimminKof("a-b*c - (x - - - (d*e))")//
        .gives("a  - b*c -x + d*e");
  }

  @Test public void b() {
    trimminKof("a-b*c")//
        .stays();
  }

  @Test public void c() {
    trimminKof("a + (b-c)")//
        .stays();
  }

  @Test public void d() {
    trimminKof("a - (b-c)")//
        .gives("a - b + c");
  }
}
