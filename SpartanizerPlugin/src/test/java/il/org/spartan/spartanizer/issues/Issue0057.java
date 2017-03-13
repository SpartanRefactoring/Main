package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
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
