package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil // put your name here
 * @since 2016 // put the year/date here */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue228 {
  @Test public void a() {
    trimmingOf("  private static A a(final B<C> b, final D c) {" + "    long d = 1;" + "    for (final C e : b) {" + "      if (!f.g(e))"
        + "        return null;" + "      E h = i.l(e);" + "      if (h == null)" + "        return null;" + "      d *= h.m(); " + "    }"
        + "    return c.n().o(E.p(d) + \"L\");" + "  }")
            .gives("  private static A a(final B<C> b, final D c) {" + "    long $ = 1;" + "    for (final C e : b) {" + "      if (!f.g(e))"
                + "        return null;" + "      E h = i.l(e);" + "      if (h == null)" + "        return null;" + "      $ *= h.m(); " + "    }"
                + "    return c.n().o(E.p($) + \"L\");" + "  }")
            .stays();
  }
}
