package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Tests for the GitHub issue thus numbered
 * @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue0879 {
  @Test public void a() {
    trimminKof("void f(){int x; int y;return;}")//
        .gives("void f(){int x,y;}")//
        .gives("void f(){}")//
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-30-16:21:39-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void daIntb4Ifb3b3b4Elseb5b55Return() {
    trimminKof("void a() { int b = 4; if (b > 3) { b += 3; b += 4; } else { b += 5; b += 55; } return; }") //
        .using(Assignment.class, new AssignmentUpdateAndSameUpdate()) //
        .gives("void a(){int b=4;if(b>3){b+=3+4;}else{b+=5+55;}return;}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("void a(){int b=4;if(b>3)b+=3+4;else b+=5+55;return;}") //
        .using(InfixExpression.class, new InfixAdditionEvaluate()) //
        .gives("void a(){int b=4;if(b>3)b+=7;else b+=60;return;}") //
        .gives("void a(){int b=4;b+=b>3?7:60;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableIntializedUpdateAssignment()) //
        .gives("void a(){int b=4+(4>3?7:60);}") //
        .using(VariableDeclarationFragment.class, new LocalVariableInitializedUnusedRemove()) //
        .gives("void a(){}") //
        .stays() //
    ;
  }

  /** Introduced by Yogi on Thu-Mar-30-16:33:00-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_intaIntb9Intc7b4cbbReturnc() {
    trimminKof("int a() { int b = 9; int c = 7; b += 4; c = b + b; return c; }") //
        .using(MethodDeclaration.class, new MethodDeclarationRenameReturnToDollar()) //
        .gives("int a(){int b=9;int $=7;b+=4;$=b+b;return $;}") //
        .using(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne()) //
        .gives("int a(){int b=9,$=7;b+=4;$=b+b;return $;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableIntializedUpdateAssignment()) //
        .gives("int a(){int b=9+4,$=7;$=b+b;return $;}") //
        .using(InfixExpression.class, new InfixAdditionEvaluate()) //
        .gives("int a(){int b=13,$=7;$=b+b;return $;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableIntializedAssignment()) //
        .gives("int a(){int b=13,$=b+b;return $;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableIntializedStatementReturnVariable()) //
        .gives("int a(){int b=13;return b+b;}") //
        .using(MethodDeclaration.class, new MethodDeclarationRenameReturnToDollar()) //
        .gives("int a(){int $=13;return $+$;}") //
        .using(VariableDeclarationFragment.class, new LocalInitializedReturnExpression()) //
        .gives("int a(){return 13+13;}") //
        .using(InfixExpression.class, new InfixAdditionEvaluate()) //
        .gives("int a(){return 26;}") //
        .stays() //
    ;
  }

  @Test public void test0() {
    trimminKof("void f(){return;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void test3() {
    trimminKof("void f(){int x=9;int y=7; x+=4;y=x+x;return;}")//
        .gives("void f(){int x=9,y=7;x+=4;y=x+x;}") //
        .gives("void f(){int x=9+4,y=7;y=x+x;}") //
        .gives("void f(){int y=7;y=(9+4)+(9+4);}") //
        .gives("void f(){int y=(9+4)+(9+4);}") //
        .gives("void f(){}") //
        .stays();
  }
}
