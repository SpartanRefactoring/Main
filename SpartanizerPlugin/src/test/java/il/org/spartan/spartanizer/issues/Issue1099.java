package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests of
 * @author Yossi Gil
 * @since 2017-01-17 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1099 {
  @Test public void a() {
    trimmingOf("/**/" + //
        "    @A public void a() {" + //
        "      final B b = a + \"\", c = C.d(b), e = F.f(g, c);" + //
        "      h.i( j, c, k(l(e)));" + //
        "      final B m = C.n(e);" + //
        "      h.i( j, m, k(l(b)));" + //
        "      h.i(j, o.p(b), k(l(o.p(m))));" + //
        "    }"//
    ).stays();
  }
  @Test public void b() {
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
  @Test public void c() {
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
