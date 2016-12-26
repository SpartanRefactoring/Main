package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue999 {
  @Test public void a() {
    expandingOf("a = b = 3;").gives("b = 3; a = b;");
  }
  @Test public void b() {
    expandingOf("a = b = c = 3;")
    .gives("c = 3; a = b = c;")
    .gives("c = 3; b = c; a = b;")
    .stays();
  }
  
  @Test public void c() {
    expandingOf("a += b += 3;")
    .gives("b += 3; a += b;")
    .gives("b = b + 3; a += b;");
  }
}
