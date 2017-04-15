package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0057 {
  @Test public void a() {
    trimminKof("void m(List<Expression>... expressions) { }")//
        .gives("void m(List<Expression>... xss) {}");
  }

  @Test public void b() {
    trimminKof("void m(Expression... expression) { }")//
        .gives("void m(Expression... xs) {}");
  }
}
