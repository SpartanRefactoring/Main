package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Unit tests of
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1101 {
  @Test public void a() {
    trimmingOf("/**/" + //
        "  boolean a(final A b, final A c) {" + //
        "    final String off = off(wrap), $ = trivia.accurateEssence(inner);" + //
        "    final String essence2 = trivia.accurateEssence(off);" + //
        "    assert essence2 != null;" + //
        "    return essence2.contains($);" + //
        "  } "//
    ).gives(
        // Edit this to reflect your expectation
        "/**/" + //
            "  boolean a(final A b, final A c) {" + //
            "    final String $ = trivia.accurateEssence(inner);" + //
            "    final String essence2 = trivia.accurateEssence(off(wrap));" + //
            "    assert essence2 != null;" + //
            "    return essence2.contains($);" + //
            "  } "//
    )//
        .stays();
  }
  private boolean accurateContains(final String wrap, final String inner) {
    final String $ = trivia.accurateEssence(inner), essence2 = trivia.accurateEssence(off(wrap));
    assert essence2 != null;
    return essence2.contains($);
  }
  private String off(String wrap) {
    // TODO Auto-generated method stub
    return null;
  }
}
