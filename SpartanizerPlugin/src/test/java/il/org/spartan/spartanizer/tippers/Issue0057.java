package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
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
