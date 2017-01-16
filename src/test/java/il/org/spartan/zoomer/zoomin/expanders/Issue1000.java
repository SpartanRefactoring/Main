package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link ExtractExpressionFromReturn}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
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
