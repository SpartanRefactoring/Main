package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class Issue0292 {
  @Test public void stringfFinalStringBuffersbNewStringBufferStringlineForlinereaderreadLineNullsbappendlineReturnsb() {
    trimmingOf(
        //
        "A a() {" + //
            " final B b = new B();" + //
            " A c;" + //
            " for (; (c = d.e()) != null; b.f(c))" + //
            "   ;" + //
            " return b + \"\";" + //
            "}"//
    ).gives( // Edit this to reflect your expectation
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
