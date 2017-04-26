package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0131 {
  @Test public void a01() {
    trimmingOf("for(int i=4; i<s.g() ; ++i){i+=9;return x;}return x;")//
        .gives("for(int i=4; i<s.g() ; ++i){i+=9;break;}return x;");
  }

  @Test public void a02() {
    trimmingOf("while(i>9)if(i==5)return x;return x;")//
        .gives("while(i>9)if(i==5)break;return x;");
  }

  @Test public void a03() {
    trimmingOf("for(int ¢=4 ; ¢<s.g() ; ++¢)return x;return x;")//
        .gives("for(int ¢=4 ; ¢<s.g() ; ++¢)break;return x;")//
        .stays();
  }

  @Test public void a04() {
    trimmingOf("for(int ¢=4 ; ¢<s.g() ; ++¢)if(t=4)return x;return x;").gives("for(int ¢=4 ; ¢<s.g() ; ++¢)if(t=4)break;return x;")//
    ;
  }

  @Test public void a05() {
    trimmingOf("while(i>5){i+=9;i++;return x;}return x;")//
        .gives("while(i>5){i+=9;i++;break;}return x;");
  }

  @Test public void a06() {
    trimmingOf("while(i>5){i+=9;return x;}return x;")//
        .gives("while(i>5){i+=9;break;}return x;")//
        .stays();
  }

  @Test public void a07() {
    trimmingOf("while(i>5)return x;return x;")//
        .gives("while(i>5)break;return x;")//
        .stays();
  }

  @Test public void a08() {
    trimmingOf("while(i>5)if(t=4)return x;return x;")//
        .gives("while(i>5)if(t=4)break;return x;");
  }

  @Test public void a09() {
    trimmingOf("for(int i=4 ; i<s.g() ; ++i)if(i==5)return x;return x;")//
        .gives("for(int i=4 ; i<s.g() ; ++i)if(i==5)break;return x;");
  }

  @Test public void a10() {
    trimmingOf("for(int i=4;i<s.g();++i){i+=9;i++;return x;}return x;")//
        .gives("for(int i=4;i<s.g();++i){i+=9;i++;break;}return x;").gives("for(int ¢=4;¢<s.g();++¢){¢+=9;¢++;break;}return x;");
  }

  /** Introduced by Yogi on Tue-Apr-11-23:01:29-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void voidaIntb5Intc2ForIntd4defdIfcdb9Returngh15ReturngReturng() {
    trimmingOf(
        "void a() { int b = 5; int c = 2; for (int d = 4; d < e.f(); ++d) { if (c == d) { b += 9; return g; } h += 15; return g; } return g; }") //
            .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
            .gives("void a(){int b=5,c=2;for(int d=4;d<e.f();++d){if(c==d){b+=9;return g;}h+=15;return g;}return g;}") //
            .using(new LocalInitializedStatementToForInitializers(), VariableDeclarationFragment.class) //
            .gives("void a(){for(int b=5,c=2,d=4;d<e.f();++d){if(c==d){b+=9;return g;}h+=15;return g;}return g;}") //
            .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
            .gives("void a(){for(int b=5,c=2,d=4;d<e.f();++d){if(c==d){b+=9;break;}h+=15;return g;}return g;}") //
            .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
            .gives("void a(){for(int b=5,c=2,d=4;d<e.f();++d){if(c==d){b+=9;break;}h+=15;break;}return g;}") //
            .using(new IfStatementBlockSequencerBlockSameSequencer(), IfStatement.class) //
            .gives("void a(){for(int b=5,c=2,d=4;d<e.f();++d){if(c==d){b+=9;}else{h+=15;}break;}return g;}") //
            .using(new BlockSingletonEliminate(), Block.class) //
            .gives("void a(){for(int b=5,c=2,d=4;d<e.f();++d){if(c==d)b+=9;else h+=15;break;}return g;}") //
            .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-23:02:38-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void booleanaFalseForIntb4bcdbIfb5e9ReturnfElseReturngh15ReturnfReturnf() {
    trimmingOf("boolean a = false; for (int b = 4; b < c.d(); ++b) { if (b == 5) { e += 9; return f; } else return g; h += 15; return f; } return f;") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("for(int b=4;b<c.d();++b){if(b==5){e+=9;return f;}else return g;h+=15;return f;}return f;") //
        .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
        .gives("for(int b=4;b<c.d();++b){if(b==5){e+=9;break;}else return g;h+=15;return f;}return f;") //
        .using(new ForFiniteConvertReturnToBreak(), ForStatement.class) //
        .gives("for(int b=4;b<c.d();++b){if(b==5){e+=9;break;}else return g;h+=15;break;}return f;") //
        .using(new IfThenOrElseIsCommandsFollowedBySequencer(), IfStatement.class) //
        .gives("for(int b=4;b<c.d();++b){if(b!=5)return g;e+=9;break;h+=15;break;}return f;") //
        .using(new SequencerNotLastInBlock<BreakStatement>(), BreakStatement.class) //
        .gives("for(int b=4;b<c.d();++b){if(b!=5)return g;e+=9;break;break;}return f;") //
        .using(new SequencerNotLastInBlock<BreakStatement>(), BreakStatement.class) //
        .gives("for(int b=4;b<c.d();++b){if(b!=5)return g;e+=9;break;}return f;") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Tue-Apr-11-17:34:42-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void publicStaticVoidaWhileb7Ifb5c9ReturndElseReturnef15ReturndReturnd() {
    trimmingOf("a() { while (b < 7) { if (b == 5) { c += 9; return d; } else return e; f += 15; return d; } return d; }") //
        .using(new WhileFiniteReturnToBreak(), WhileStatement.class) //
        .gives("a(){while(b<7){if(b==5){c+=9;break;}else return e;f+=15;return d;}return d;}") //
        .using(new WhileFiniteReturnToBreak(), WhileStatement.class) //
        .gives("a(){while(b<7){if(b==5){c+=9;break;}else return e;f+=15;break;}return d;}") //
        .using(new IfThenOrElseIsCommandsFollowedBySequencer(), IfStatement.class) //
        .gives("a(){while(b<7){if(b!=5)return e;c+=9;break;f+=15;break;}return d;}") //
        .using(new SequencerNotLastInBlock<BreakStatement>(), BreakStatement.class) //
        .gives("a(){while(b<7){if(b!=5)return e;c+=9;break;break;}return d;}") //
        .using(new SequencerNotLastInBlock<BreakStatement>(), BreakStatement.class) //
        .gives("a(){while(b<7){if(b!=5)return e;c+=9;break;}return d;}") //
        .stays() //
    ;
  }
}
