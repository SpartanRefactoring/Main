package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

import il.org.spartan.athenizer.zoomers.ExtractExpressionFromReturn;

/** Unit test for {@link ExtractExpressionFromReturn}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue1000 {
  @Test public void a() {
    bloatingOf("return a = 3;")//
        .gives("a=3; return a;");
  }
  @Test public void b() {
    bloatingOf("return a = b = 3;")//
        .gives("a = b = 3; return a;");
  }
  @Test public void c() {
    bloatingOf("return a += b += f();")//
        .gives("a += b += f(); return a;");
  }
}
