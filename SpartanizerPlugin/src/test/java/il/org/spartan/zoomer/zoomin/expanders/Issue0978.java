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

  @Test public void b() {
    bloatingOf("int main() {  new Integer(7); }")//
        .gives("int main() { Integer i1 = new Integer(7); }");
  }

  @Test public void c() {
    bloatingOf("new Integer(7);")//
        .gives("Integer i1 = new Integer(7);");
  }

  @Test public void d() {
    bloatingOf("new Integer(5); new Integer(7);").gives("Integer i1 = new Integer(5); new Integer(7);")
        .gives("Integer i1; i1 = new Integer(5); new Integer(7);").gives("Integer i1; i1 = new Integer(5); Integer i2 = new Integer(7);");
  }
}
