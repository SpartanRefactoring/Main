package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue0292 {
  @Test public void imporant() {
    trimmingOf( //
        "A a() {" + //
            " final B b = new B();" + //
            " A c;" + //
            " for (; (c = d.e()) != null; b.f(c))" + //
            "   ;" + //
            " return b + \"\";" + //
            "}"//
    ).gives( //
        "A a() {" + //
            " final B $ = new B();" + //
            " A c;" + //
            " for (; (c = d.e()) != null; $.f(c))" + //
            "   ;" + //
            " return $ + \"\";" + //
            "}"//
    //
    ).gives( //
        "A a() {" + //
            " final B $ = new B();" + //
            " for (A c = d.e(); c != null; $.f(c))" + //
            "   ;" + //
            " return $ + \"\";" + //
            "}"//
    //
    )//
        .gives( //
            "A a() {" + //
                " final B $ = new B();" + //
                " for (A c = d.e();;$.f(c))" + //
                "   if (c == null)" + //
                "     return $ + \"\";" + //
                "}"//
        )//
        .stays();
  }
}
