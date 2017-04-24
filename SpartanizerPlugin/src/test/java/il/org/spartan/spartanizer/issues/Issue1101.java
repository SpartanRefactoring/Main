package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests of
 * @author Yossi Gil
 * @since 2017-01-17 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1101 {
  @Ignore @Test public void a() {
    trimminKof("/**/" + //
        "  boolean a(final A b, final A c) {" + //
        "    final String off = off(wrap), $ = Trivia.accurateEssence(inner);" + //
        "    final String essence2 = Trivia.accurateEssence(off);" + //
        "    assert essence2 != null;" + //
        "    return essence2.contains($);" + //
        "  } "//
    ).gives(
        // Edit this to reflect your expectation
        "/**/" + //
            "  boolean a(final A b, final A c) {" + //
            "    final String $ = Trivia.accurateEssence(inner);" + //
            "    final String essence2 = Trivia.accurateEssence(off(wrap));" + //
            "    assert essence2 != null;" + //
            "    return essence2.contains($);" + //
            "  } "//
    )//
        .stays();
  }
}
