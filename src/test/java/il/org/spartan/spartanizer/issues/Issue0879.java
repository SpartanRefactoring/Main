package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests for the GitHub issue thus numbered
 * @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue0879 {
  @Test public void a() {
    trimmingOf("void f(){int x; int y;return;}")//
        .gives("void f(){int x,y;}")//
        .gives("void f(){}")//
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-30-16:21:39-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void daIntb4Ifb3b3b4Elseb5b55Return() {
    trimmingOf("void a() { int b = 4; if (b > 3) { b += 3; b += 4; } else { b += 5; b += 55; } return; }") //
        .using(new AssignmentUpdateAndSameUpdate(), Assignment.class) //
        .gives("void a(){int b=4;if(b>3){b+=3+4;}else{b+=5+55;}return;}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("void a(){int b=4;if(b>3)b+=3+4;else b+=5+55;return;}") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("void a(){int b=4;if(b>3)b+=7;else b+=60;return;}") //
        .gives("void a(){int b=4;b+=b>3?7:60;}") //
        .using(new LocalInitializedUpdateAssignment(), VariableDeclarationFragment.class) //
        .gives("void a(){int b=4+(4>3?7:60);}") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("void a(){}") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Thu-Mar-30-16:33:00-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Ignore @Test public void test_intaIntb9Intc7b4cbbReturnc() {
    trimmingOf("int a() { int b = 9; int c = 7; b += 4; c = b + b; return c; }") //
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .gives("int a(){int b=9;int $=7;b+=4;$=b+b;return $;}") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a(){int b=9,$=7;b+=4;$=b+b;return $;}") //
        .using(new LocalInitializedUpdateAssignment(), VariableDeclarationFragment.class) //
        .gives("int a(){int b=9+4,$=7;$=b+b;return $;}") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("int a(){int b=13,$=7;$=b+b;return $;}") //
        .using(new LocalUninitializedAssignment(), VariableDeclarationFragment.class) //
        .gives("int a(){int b=13,$=b+b;return $;}") //
        .using(new LocalInitializedStatementReturnVariable(), VariableDeclarationFragment.class) //
        .gives("int a(){int b=13;return b+b;}") //
        .using(new MethodDeclarationRenameReturnToDollar(), MethodDeclaration.class) //
        .gives("int a(){int $=13;return $+$;}") //
        .using(new LocalInitializedReturnExpression(), VariableDeclarationFragment.class) //
        .gives("int a(){return 13+13;}") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("int a(){return 26;}") //
        .stays() //
    ;
  }

  @Test public void test0() {
    trimmingOf("void f(){return;}")//
        .gives("void f(){}")//
        .stays();
  }

  
@Test public void test3() {
    trimmingOf("void f(){int x=9;int y=7; x+=4;y=x+x;return;}")//
        .gives("void f(){int x=9,y=7;x+=4;y=x+x;}") //
        .gives("void f(){int x=9+4,y=7;y=x+x;}") //
        .gives("void f(){int y=7;y=(9+4)+(9+4);}") //
        .gives("void f(){int y=7;y=9+4+9+4;}") //
        .gives("void f(){int y=7;y=26;}") //
        .stays();
  }
}
