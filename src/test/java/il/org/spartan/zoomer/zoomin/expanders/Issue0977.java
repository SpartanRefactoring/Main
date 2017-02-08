package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link CasesSplit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0977 {
  @Test public void simpleSequencers() {
    bloatingOf("switch (x){case 1:f(1);case 2:f(2);break;case 3:f(3);case 4:" + " throw new Exception();default:}")
        .gives("switch (x){case 1:f(1);f(2);break;case 2:f(2);break;case 3:" + " f(3);case 4:throw new Exception();default:}")
        .gives("switch (x){case 1:f(1);f(2);break;case 2:f(2);break;case 3:" + " f(3);throw new Exception();case 4:throw new Exception();default:}")
        .stays();
  }

  @Test public void complexMerging() {
    bloatingOf("switch (x){case 1:f(1);case 2:f(2);default:f(3);throw new Exception();}")
        .gives("switch (x){case 1:f(1);f(2);f(3);throw new Exception();case 2:f(2);" + "default:f(3);throw new Exception();}")
        .gives("switch (x){case 1:f(1);f(2);f(3);throw new Exception();case 2:f(2);"
            + " f(3);throw new Exception();default:f(3);throw new Exception();}")
        .stays();
  }

  // see issue #1046
  @Test public void complexSequencer() {
    bloatingOf("switch (a()){case 1:if (b()){return c();} else{throw d();} case 2:" + " e();}")//
        .stays();
  }

  // see issue #1046
  @Test public void complexSequencerNotLast() {
    bloatingOf("switch (a()){case 1:if (b()){return c();} else{throw d();} f();" + "case 2:e();}")//
        .stays();
  }

  // see issue #1031
  @Test public void complexSequencerRealWorld() {
    bloatingOf("switch (a()){case 1:if (!parameters((MethodDeclaration) $).contains(¢)){return Kind.method;} else{"
        + " return Kind.parameter;} case 2:e();}")//
            .stays();
  }
}
