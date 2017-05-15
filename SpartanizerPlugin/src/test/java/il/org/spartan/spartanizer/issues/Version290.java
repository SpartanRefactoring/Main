package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** @author Yossi Gil
 * @since 2014-07-10 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Version290 {
  public void doNotInlineDeclarationWithAnnotationSimplified() {
    trimmingOf("@SuppressWarnings()int $=(Class<T>)findClass(className); ")//
        .stays();
  }
  /** Introduced by Yogi on Wed-Apr-12-10:49:02-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void intab0Intc3Intdc2Intedc19be2dea() {
    trimmingOf("int a = b(0); int c = 3; int d = c + 2; int e = d + c - 19; b(e * 2 - d / e + a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=b(0),c=3;int d=c+2,e=d+c-19;b(e*2-d/e+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=b(0),c=3,d=c+2,e=d+c-19;b(e*2-d/e+a);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int c=3,d=c+2,e=d+c-19;b(e*2-d/e+b(0));") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("int c=3,d=c+2,e=c+d-19;b(e*2-d/e+b(0));") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int c=3,d=c+2,e=c+d-19;b(2*e-d/e+b(0));") //
        .stays() //
    ;
  }
  /** Introduced by Yogi on Wed-Apr-12-10:51:24-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta5b3Intcb2Intdcb19aec() {
    trimmingOf("int a = 5, b = 3; int c = b + 2; int d = c + b - 19 + a; e(c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2;int d=c+b-19+a;e(c);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2,d=c+b-19+a;e(c);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int a=5,b=3,c=b+2;e(c);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3,c=b+2;e(c);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3;e((b+2));") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("e((3+2));") //
        .using(new ParenthesizedRemoveExtraParenthesis(), ParenthesizedExpression.class) //
        .gives("e(3+2);") //
        .using(new InfixAdditionEvaluate(), InfixExpression.class) //
        .gives("e(5);") //
        .stays() //
    ;
  }
  /** Introduced by Yogi on Wed-Apr-12-10:53:05-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta5Intb3Intcb2Intdcb19ed2cda() {
    trimmingOf("int a = 5; int b = 3; int c = b + 2; int d = c + b - 19; e(d * 2 - c / d + a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3;int c=b+2,d=c+b-19;e(d*2-c/d+a);") //
        .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
        .gives("int a=5,b=3,c=b+2,d=c+b-19;e(d*2-c/d+a);") //
        .using(new LocalInitializedStatementTerminatingScope(), VariableDeclarationFragment.class) //
        .gives("int b=3,c=b+2,d=c+b-19;e(d*2-c/d+5);") //
        .using(new InfixAdditionSort(), InfixExpression.class) //
        .gives("int b=3,c=b+2,d=b+c-19;e(d*2-c/d+5);") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int b=3,c=b+2,d=b+c-19;e(2*d-c/d+5);") //
        .stays() //
    ;
  }
  /** Introduced by Yogi on Wed-Apr-12-11:06:45-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void inta6FinalAbNewAaIntc2dccacca() {
    trimmingOf("int a = 6; final A b = new A(a); int c = 2 + d; c(c + a); c(c * a);") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c+a);c(c*a);") //
        .using(new InfixMultiplicationSort(), InfixExpression.class) //
        .gives("int a=6;new A(a);int c=2+d;c(c+a);c(a*c);") //
        .stays() //
    ;
  }
  @Test public void renameVariableUnderscore2() {
    trimmingOf("class A{int __;int f(int _){return _;}}")//
        .gives("class A{int __;int f(int __){return __;}}") //
        .stays();
  }
  @Test public void replaceClassInstanceCreationWithFactoryClassInstanceCreation() {
    trimmingOf("Character x=new Character(new Character(f()));")//
        .gives("new Character(new Character(f()));")//
        .gives("Character.valueOf(new Character(f()));") //
        .gives("Character.valueOf(Character.valueOf(f()));") //
        .stays();
  }
}