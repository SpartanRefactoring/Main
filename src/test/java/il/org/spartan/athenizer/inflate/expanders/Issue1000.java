package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;
import org.junit.*;

/** @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue1000 {
  @Test public void a() {
    expandingOf("return a = 3;").gives("a=3; return a;");
  }
  
  @Test public void b() {
    expandingOf("return a = b = 3;").gives("a = b = 3; return a;");
  }
  
  @Test public void c() {
    expandingOf("return a += b += f();").gives("a += b += f(); return a;");
  }
}
