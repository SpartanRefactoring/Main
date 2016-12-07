package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Tomer Dragucki
 * @since 2016 **/
@SuppressWarnings({ "static-method", "javadoc" })
public class issue289 {
  @Test public void a() {
    trimmingOf("void f() {" + "  final Object[] os = new Integer[1];" + "  os[0] = new Object(); }").stays();
  }

  @Test public void b() {
    trimmingOf("public static void test() {" + "  final XTestArray01[] array = new TestArrayAccess01[1];" + "  array[0] = new XTestArray01(); }")
        .stays();
  }

  @Test public void c() {
    trimmingOf("void f() {" + "  final Object[] os = new Object[1];" + "  os[0] = new Object(); }").gives("void f() {" + //
        "  (new Object[1])[0] = new Object();" + //
        "     }").stays();
  }
}
