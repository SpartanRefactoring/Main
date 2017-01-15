package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

/** @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-15 */
@SuppressWarnings("static-method")
public class Issue0978 {
  @Test public void a() {
    bloatingOf("class Z {} int main() {  new Z(); }")//
        .gives("class Z {} int main() { Z z1 = new Z(); }");
  }
}
