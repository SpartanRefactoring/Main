package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0082 {
  @Test public void a() {
    trimminKof("(long)5")//
        .gives("1L*5");
  }

  @Test public void b() {
    trimminKof("(long)(int)a")//
        .gives("1L*(int)a")//
        .stays();
  }

  @Test public void b_a_cuold_be_double() {
    trimminKof("(long)a")//
        .stays();
  }

  @Test public void c() {
    trimminKof("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }

  @Test public void d() {
    trimminKof("(long)a*(long)b")//
        .stays();
  }

  @Test public void e() {
    trimminKof("(double)(long)a")//
        .gives("1.*(long)a")//
        .stays();
  }
}
