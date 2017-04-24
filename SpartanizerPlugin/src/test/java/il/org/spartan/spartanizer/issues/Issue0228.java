package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Dan Abramovich
 * @since 30-11-2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0228 {
  @Test public void a() {
    trimminKof(" private static A a(final B<C> b, final D c) {    long d = 1;    for (final C e : b) {      if (!f.g(e))"
        + "        return null;      E h = i.l(e);      if (h == null)        return null;      d *= h.m();     }"
        + "    return c.n().o(E.p(d) + \"L\");  }")
            .gives(" private static A a(final B<C> b, final D c) {    long $ = 1;    for (final C e : b) {      if (!f.g(e))"
                + "        return null;      E h = i.l(e);      if (h == null)        return null;      $ *= h.m();     }"
                + "    return c.n().o(E.p($) + \"L\");  }")
            .stays();
  }
}
