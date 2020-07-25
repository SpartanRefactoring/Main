package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.IfStatement;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.IfExpressionStatementElseSimilarExpressionStatement;
import il.org.spartan.spartanizer.tippers.IfStatementBlockSequencerBlockSameSequencer;

/** Unit tests of {@link IfStatementBlockSequencerBlockSameSequencer}
 * @author Yossi Gil
 * @since Jan 22, 2017 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1105 {
  /** Introduced by Yogi on Fri-Mar-31-00:33:42-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_protectedAaFinalBbcdefIfbNullghijReturnggkbChijReturng() {
    trimmingOf("protected A a() { final B b = c.d(e(f)); if (b == null) { g.h(i, j); return g; } g.k(b, C).h(i, j); return g; }") //
        .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
        .gives("protected A a(){final B b=c.d(e(f));if(b==null){g.h(i,j);}else{g.k(b,C).h(i,j);}return g;}") //
        .using(new IfExpressionStatementElseSimilarExpressionStatement(), IfStatement.class) //
        .gives("protected A a(){final B b=c.d(e(f));(b==null?g:g.k(b,C)).h(i,j);return g;}") //
        .stays() //
    ;
  }
  @Test public void Issue1412() {
    trimmingOf("protected char[] escape(int cp) {" + //
        "if (cp <= 0xffff) {" + //
        "char[] dest = new char[9];" + //
        "dest[0] = '%';" + //
        "return dest;" + //
        "}" + //
        "if (cp > 0x10ffff)" + //
        "throw new IllegalArgumentException(\"Invalid unicode character value \" + cp);" + //
        "char[] dest = new char[12];" + //
        "dest[0] = '%';" + //
        "return dest;" + //
        "}") //
            .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
            .stays(); //
  }
  @Test public void Issue1412_2() {
    trimmingOf("protected A a() { final B b = c.d(e(f)); if (b == null) { int t = g.h(i, j); return t; } int t = g.k(b, C).h(i, j); return t; }") //
        .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
        .stays(); //
  }
}
