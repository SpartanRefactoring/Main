package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@Ignore
//naming...
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0057 {
  @Test public void a() {
    trimmingOf("void m(List<Expression>... expressions) { }")//
        .gives("void m(List<Expression>... xss) {}");
  }
  @Test public void b() {
    trimmingOf("void m(Expression... expression) { }")//
        .gives("void m(Expression... xs) {}");
  }
}
