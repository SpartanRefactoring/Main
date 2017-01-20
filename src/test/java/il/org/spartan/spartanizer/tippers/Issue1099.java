package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests of
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1099 {
  @Test public void a() {
    trimmingOf("/**/" + //
        "  " + //
        "    final D c = b.d(), e = f.g(e(b)), h = f.g(h(b));" + //
        "    return e == null || h == null ? null : i.j(i.k(e, h).l(c)).m();" + //
        "  "//
    )//
        .gives("/**/" + //
            "  " + //
            "    final D e = f.g(e(b)), h = f.g(h(b));" + //
            "    return e == null || h == null ? null : i.j(i.k(e, h).l(b.d())).m();" + //
            "  "//
        )//
        .stays();
  }

  @Test public void b() {
    trimmingOf("/**/" + //
        "  " + //
        "    final D c = d(), e = f(c,c), h = g(e, c);" + //
        "    return c + e + h; " + //
        "  "//
    )//
        .gives("/**/" + //
            "  " + //
            "    final D c = d(), e = f(c,c);" + //
            "    return c + e + g(e,c); " + //
            "  "//
        )//
        .stays();
  }
}
