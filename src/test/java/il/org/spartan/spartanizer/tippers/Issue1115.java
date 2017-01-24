package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests of {@link IfStatementBlockSequencerBlockSameSequencer}
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since Jan 23, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1115 {
  @Test public void a() {
    trimmingOf("/**/" + //
        "  protected B a() {" + //
        "    final F f = g.h(i(c));" + //
        "    if (f == null) {" + //
        "      b.j(d, e);" + //
        "      return b;" + //
        "    }" + //
        "    b.k(f, G).j(d, e);" + //
        "    return b;" + //
        "  }"//
    ).gives(
        // Edit this to reflect your expectation
        "/**/" + //
            "  B a() {" + //
            "    final F f = g.h(i(c));" + //
            "    if (f == null) {" + //
            "      b.j(d, e);" + //
            "      return b;" + //
            "    }" + //
            "    b.k(f, G).j(d, e);" + //
            "    return b;" + //
            "  }"//
    )//
        .stays();
  }
}
