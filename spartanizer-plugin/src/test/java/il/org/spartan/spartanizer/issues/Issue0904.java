package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** This is a unit test for {@link BlockRemoveDeadVariables} of previously
 * failed tests. Related to {@link Issue0359}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0904 {
  @Test public void a() {
    trimmingOf("int i;")//
        .gives("")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("int i,j=1;j++;f(++j);")//
        .gives("int j=1;++j;f(++j);")//
        .gives("int j=1+1;f(++j);") //
        .gives("int j=2;f(++j);") //
        .stays();
  }
  @Test public void d() {
    trimmingOf("int i; i = +0;")//
        .gives("int i =+0;")//
        .gives("")//
        .stays();
  }
  @Test public void t08() {
    trimmingOf("if(b){int i;int j;}else{g();int tipper;}")//
        .gives("if(b){}else{g();}") //
        .gives("if(!b)g();") //
        .stays()//
    ;
  }
  @Test public void t09() {
    trimmingOf("if(b){int i;int j;g();}else{int q;int tipper;}")//
        .gives("if(!b){int q;int tipper;}else{int i;int j;g();}")//
        .gives("if(!b){}else{int i,j; g();}") //
        .gives("if(b){int i,j;g();}") //
        .gives("if(!b)return;int i,j;g();") //
        .gives("if(!b)return;int i;g();") //
        .gives("if(!b)return;g();") //
        .gives("if(!b);else g();") //
        .gives("if(b)g();") //
        .stays();
  }
}
