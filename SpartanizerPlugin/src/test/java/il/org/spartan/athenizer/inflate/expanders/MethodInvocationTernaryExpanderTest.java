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
}
