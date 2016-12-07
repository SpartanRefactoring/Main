package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Dan Abramovich
 * @since 30-11-2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue885 {
  @Test public void a() {
    trimmingOf("  @A protected B a(final B b, final C c, final D d, final E e," + "      final F f, final G g) {" + "    if (e == null || h.i(c))"
        + "      return null;" + "    final H j = k.l(f);" + "    if (j == null)" + "      return null;" + "    final I m = k.n(o.p(j));"
        + "    if (m == null || !q.r(d, s(m)) || m.t() != J)" + "      return null;" + "    final E u = v.w(x(m));"
        + "    final K y = new L(d, b, g).z(e);" + "    if (!y.x1(u) || y.x50(u) - x54(c) - x54.x54(u) > 0)" + "      return null;"
        + "    b.x54(m, u, g);" + "    y.x54(u);" + "    x54(c, b, g);" + "    return b;" + "  }")
            .gives("  @A protected B a(final B $, final C c, final D d, final E e," + "      final F f, final G g) {" + "    if (e == null || h.i(c))"
                + "      return null;" + "    final H j = k.l(f);" + "    if (j == null)" + "      return null;" + "    final I m = k.n(o.p(j));"
                + "    if (m == null || !q.r(d, s(m)) || m.t() != J)" + "      return null;" + "    final E u = v.w(x(m));"
                + "    final K y = new L(d, $, g).z(e);" + "    if (!y.x1(u) || y.x50(u) - x54(c) - x54.x54(u) > 0)" + "      return null;"
                + "    $.x54(m, u, g);" + "    y.x54(u);" + "    x54(c, $, g);" + "    return $;" + "  }")
            .stays();
  }
}
