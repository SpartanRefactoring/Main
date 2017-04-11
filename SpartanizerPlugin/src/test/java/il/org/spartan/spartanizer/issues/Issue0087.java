package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0087 {
  @Test public void a() {
    topDownTrimming("a-b*c - (x - - - (d*e))")//
        .gives("a  - b*c -x + d*e");
  }

  @Test public void b() {
    topDownTrimming("a-b*c")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("a + (b-c)")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("a - (b-c)")//
        .gives("a - b + c");
  }
}
