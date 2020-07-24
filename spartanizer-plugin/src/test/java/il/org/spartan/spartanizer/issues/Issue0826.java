package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** Unit tests for the GitHub issue thus numbered
 * @author Tomer Dragucki
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0826 {
  @Test public void a() {
    trimmingOf("A() ? 8 : 8")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("public void b() { int i = 210;   if (++i < 5)  a(i);  else  a(i);")//
        .stays();
  }
}
