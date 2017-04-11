package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

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
    trimminKof("if(b()){int i;}")//
        .gives("if(b()){}")//
    ;
  }

  /** Introduced by Yogi on Mon-Mar-27-00:35:21-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_ifaTrueIntb5cd() {
    trimminKof("if (a == true) { int b = 5, c = d(); }") //
        .using(InfixExpression.class, new InfixComparisonBooleanLiteral()) //
        .gives("if(a){int b=5,c=d();}") //
        .using(VariableDeclarationFragment.class, new LocalVariableInitializedUnusedRemove()) //
        .gives("if(a){d(); int b=5;}") //
        .using(VariableDeclarationFragment.class, new LocalVariableInitializedUnusedRemove()) //
        .gives("if(a){d();}") //
        .using(Block.class, new BlockSingleton()) //
        .gives("if(a)d();") //
        .stays() //
    ;
  }

  @Test public void t15() {
    trimminKof("if(b==q()){int i;}")//
        .gives("if(b==q()){}")//
        .gives("q();")//
        .stays() //
    ;
  }

  @Test public void t17() {
    trimminKof("while(b==q){if(tipper==q()){int i;}}")//
        .gives("while(b==q)if(tipper==q()){int i;}")//
        .gives("while(b==q)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t21() {
    trimminKof("for(i=1;b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q;++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t23() {
    trimminKof("for(i=1;b==q();++i){if(tipper==q()){int i;}}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){int i;}")//
        .gives("for(i=1;b==q();++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t24() {
    trimminKof("for(i=tipper();b==q;++i){if(tipper==q()){int i;}}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){int i;}")//
        .gives("for(i=tipper();b==q;++i)if(tipper==q()){}")//
        .stays();
  }

  @Test public void t25() {
    trimminKof("for(i=4;b==q;f=i()){if(tipper==q()){int i;}}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){int i;}")//
        .gives("for(i=4;b==q;f=i())if(tipper==q()){}")//
        .stays();
  }
}
