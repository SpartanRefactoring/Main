package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0082 {
  @Test public void a() {
    topDownTrimming("(long)5")//
        .gives("1L*5");
  }

  @Test public void b() {
    topDownTrimming("(long)(int)a")//
        .gives("1L*(int)a")//
        .stays();
  }

  @Test public void b_a_cuold_be_double() {
    topDownTrimming("(long)a")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("(long)a*(long)b")//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("(double)(long)a")//
        .gives("1.*(long)a")//
        .stays();
  }
}
