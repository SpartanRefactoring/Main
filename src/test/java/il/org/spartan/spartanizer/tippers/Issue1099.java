package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests of 
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
@Ignore 
public class Issue1099 extends MetaFixture {
  @Test public void a() {
    trimmingOf(
  "/**/" + //
  "  " + //
  "    final D c = b.d(), e = f.g(e(b)), h = f.g(h(b));" + //
  "    return e == null || h == null ? null : i.j(i.k(e, h).l(c)).m();" + //
  "  "//
)//
  .stays();
  }
}
