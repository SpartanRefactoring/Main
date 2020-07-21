package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests dollar renaming even if not only expression but also s + "" for
 * example
 * @author Yossi Gil
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0857 {
  @Test public void report234() {
    trimmingOf(//
        "public static A a() {" + //
            " A b = \"str\";" + //
            " B.d(b);" + //
            " return b + \"\";" + //
            "}"//
    )//
        .gives(//
            "public static A a() {" + //
                " A $ = \"str\";" + //
                " B.d($);" + //
                " return $ + \"\";" + //
                "}"//
        )//
        .stays();
  }
}
