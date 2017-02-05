package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link RemoveRedundantIf},
 * {@link ForDeadRemove}, {@link RemoveRedundantWhile} of previously failed
 * tests. Related to {@link Issue0251}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0905 {
  @Test public void t06() {
    trimmingOf("if(b()){int i;}")//
        .gives("if(b()){}")//
    ;
  }

  @Test public void t12() {
    trimmingOf("if(b==true){int i=5,q=g();}")//
        .gives("if(b){int i=5,q=g();}")//
        .stays() //
    ;
  }

  @Ignore // TODO Yossi Gil
  @Test public void t15() {
    trimmingOf("if(b==q()){int i;}")//
        .gives("if(b==q()){}")//
        .stays();
  }

  @Ignore // TODO Yossi Gil
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
