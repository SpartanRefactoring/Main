package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit test for {@link CasesSplit}.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @author Yuval Simon
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class Issue0977 {
  @Test public void complexMerging() {
    bloatingOf("switch (x){case 1:f(1);case 2:f(2);default:f(3);throw new Exception();}")
        .gives("switch (x){case 1:f(1);f(2);f(3);throw new Exception();case 2:f(2);default:f(3);throw new Exception();}")
        .gives("switch (x){case 1:f(1);f(2);f(3);throw new Exception();case 2:f(2);"
            + " f(3);throw new Exception();default:f(3);throw new Exception();}")
        .stays();
  }
  @Test public void complexSequencer() {
    bloatingOf("switch (a()){case 1:if (b()){return c();} else{throw d();} case 2: e(); default:}")//
        .gives("switch (a()){case 1:if (b()){return c();} else{throw d();} case 2: e(); break; default:}")//
        .stays();
  }
  @Test public void complexSequencerNotLast() {
    bloatingOf("switch (a()){case 1:if (b()){return c();} else{throw d();} f();case 2:e(); default:}")//
        .gives("switch (a()){case 1:if (b()){return c();} else{throw d();} f();case 2:e(); break; default:}")//
        .stays();
  }
  @Test public void complexSequencerRealWorld() {
    bloatingOf("switch (a()){case 1:if (!parameters((MethodDeclaration) $).contains(¢)){return Kind.method;} else{"
        + " return Kind.parameter;} case 2:e(); break; default:}")//
            .stays();
  }
  @Test public void simpleSequencers() {
    bloatingOf("switch (x){case 1:f(1);case 2:f(2);break;case 3:f(3);case 4: throw new Exception();default:}")
        .gives("switch (x){case 1:f(1);f(2);break;case 2:f(2);break;case 3: f(3);case 4:throw new Exception();default:}")
        .gives("switch (x){case 1:f(1);f(2);break;case 2:f(2);break;case 3: f(3);throw new Exception();case 4:throw new Exception();default:}")
        .stays();
  }
  @Test public void newNameInVarDeclaration() {
    bloatingOf("switch (x) { case 1: case 2: int y; y = 1; return y; }").needRenaming(false)
        .gives("switch (x) { case 1: int y1; y1 = 1; return y1; case 2: int y; y = 1; return y; }");
  }
  @Test public void newNameInVarDeclaration2() {
    bloatingOf("switch (x) { case 1: case 2: int y = f(); return y + 1; }").needRenaming(false)
        .gives("switch (x) { case 1: int y1 = f(); return y1 + 1; case 2: int y = f(); return y + 1; }");
  }
}
