package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Yossi Gil {@code yogi@cs.technion.ac.il}
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue0292 {
  @Test public void stringfFinalStringBuffersbNewStringBufferStringlineForlinereaderreadLineNullsbappendlineReturnsb() {
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
            " final B b = new B();" + //
            " for (A c; (c = d.e()) != null; b.f(c))" + //
            "   ;" + //
            " return b + \"\";" + //
            "}"//
    //
    )//
        .stays();
  }
}
