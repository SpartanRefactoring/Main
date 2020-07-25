package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0082 {
  @Test public void a() {
    trimmingOf("(long)5")//
        .gives("1L*5");
  }
  @Test public void b() {
    trimmingOf("(long)(int)a")//
        .gives("1L*(int)a")//
        .stays();
  }
  @Test public void b_a_cuold_be_double() {
    trimmingOf("(long)a")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("(long)(long)2")//
        .gives("1L*(long)2")//
        .gives("1L*1L*2")//
        .gives("2L")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("(long)a*(long)b")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("(double)(long)a")//
        .gives("1.*(long)a")//
        .stays();
  }
}
