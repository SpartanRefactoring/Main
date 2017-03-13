package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests of
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1101 {
  @Ignore @Test public void a() {
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
}
