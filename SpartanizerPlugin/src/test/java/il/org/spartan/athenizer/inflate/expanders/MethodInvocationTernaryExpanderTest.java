package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Unit tests for {@link MethodInvocationTernaryExpander}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class MethodInvocationTernaryExpanderTest {
  @Test public void a() {
    expandingOf("o.f(x ? a : b);").gives("if (x)" //
        + "  o.f(a);" //
        + "else " //
        + "  o.f(b);").stays();
  }

  @Test public void b() {
    expandingOf("o.f(p, x ? a : b);").gives("if (x)" //
        + "  o.f(p, a);" //
        + "else" //
        + "  o.f(p, b);").stays();
  }

  @Test public void c() {
    expandingOf("o.f(y ? a1 : b1, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, x ? a2 : b2);")
        .gives("if (y)" //
            + "  if (x)" //
            + "    o.f(a1, a2);" //
            + "  else" //
            + "    o.f(a1, b2);" //
            + "else" //
            + "  o.f(b1, x ? a2 : b2);")
        .gives("if (y)" //
            + "  if (x)" //
            + "    o.f(a1, a2);" //
            + "  else" //
            + "    o.f(a1, b2);" //
            + "else" //
            + "  if (x)" //
            + "    o.f(b1, a2);" //
            + "  else" //
            + "    o.f(b1, b2);")
        .stays();
  }

  @Test public void d() {
    expandingOf("o.f(y ? a1 : b1, pp, x ? a2 : b2);")
        .gives("if (y)" //
            + "  o.f(a1, pp, x ? a2 : b2);" //
            + "else" //
            + "  o.f(b1, pp, x ? a2 : b2);")
        .gives("if (y)" //
            + "  if (x)" //
            + "    o.f(a1, pp, a2);" //
            + "  else" //
            + "    o.f(a1, pp, b2);" //
            + "else" //
            + "  o.f(b1, pp, x ? a2 : b2);")
        .gives("if (y)" //
            + "  if (x)" //
            + "    o.f(a1, pp, a2);" //
            + "  else" //
            + "    o.f(a1, pp, b2);" //
            + "else" //
            + "  if (x)" //
            + "    o.f(b1, pp, a2);" //
            + "  else" //
            + "    o.f(b1, pp, b2);")
        .stays();
  }
}
