package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1042 {
  @Test public void test() {
    trimmingOf("abstract int f(final int a, final int b);")//
    .gives("abstract int f(int a, int b);")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("interface a { int f(final int a, final int b);}")//
    .gives("interface a { int f(int a, int b);}")//
        .stays();
  }
}
