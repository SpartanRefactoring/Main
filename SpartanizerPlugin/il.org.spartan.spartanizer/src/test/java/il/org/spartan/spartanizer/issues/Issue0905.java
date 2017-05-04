package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link IfDeadRemov}, {@link ForDeadRemove},
 * {@link WhileDeadRemove} of previously failed tests. Related to
 * {@link Issue0251}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0905 {
  @Test public void t06() {
    trimmingOf("if(b()){int i;}")//
        .gives("if(b()){}")//
    ;
  }

  /** Introduced by Yogi on Mon-Mar-27-00:35:21-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_ifaTrueIntb5cd() {
    trimmingOf("if (a == true) { int b = 5, c = d(); }") //
        .using(new InfixComparisonBooleanLiteral(), InfixExpression.class) //
        .gives("if(a){int b=5,c=d();}") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("if(a){d(); int b=5;}") //
        .using(new LocalInitializedUnusedRemove(), VariableDeclarationFragment.class) //
        .gives("if(a){d();}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("if(a)d();") //
        .stays() //
    ;
  }

  @Test public void t15() {
    trimmingOf("if(b==q()){int i;}")//
        .gives("if(b==q()){}")//
        .gives("q();")//
        .stays() //
    ;
  }

  @Test public void t17() {
    trimmingOf("while(b==q){if(tipper==q()){int i;}}")//
        .gives("while(b==q)if(tipper==q()){int i;}")//
        .gives("while(b==q)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t21() {
    trimmingOf("for(i=1;b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t23() {
    trimmingOf("for(i=1;b==q();++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t24() {
    trimmingOf("for(i=tipper();b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t25() {
    trimmingOf("for(i=4;b==q;f=i()){if(tipper==q()){int i;}}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){int i;}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){}")//
        .stays();
  }
}
