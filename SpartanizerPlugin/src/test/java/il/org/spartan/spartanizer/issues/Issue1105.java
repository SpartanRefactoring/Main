package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit tests of {@link IfStatementBlockSequencerBlockSameSequencer}
 * @author Yossi Gil
 * @since Jan 22, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1105 {
  /** Introduced by Yogi on Fri-Mar-31-00:33:42-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_protectedAaFinalBbcdefIfbNullghijReturnggkbChijReturng() {
    topDownTrimming("protected A a() { final B b = c.d(e(f)); if (b == null) { g.h(i, j); return g; } g.k(b, C).h(i, j); return g; }") //
        .using(IfStatement.class, new IfStatementBlockSequencerBlockSameSequencer()) //
        .gives("protected A a(){final B b=c.d(e(f));if(b==null){g.h(i,j);}else{g.k(b,C).h(i,j);}return g;}") //
        .using(IfStatement.class, new IfExpressionStatementElseSimilarExpressionStatement()) //
        .gives("protected A a(){final B b=c.d(e(f));(b==null?g:g.k(b,C)).h(i,j);return g;}") //
        .stays() //
    ;
  }
}
