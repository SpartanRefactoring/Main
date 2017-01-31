package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

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

  @Ignore // TODO: Raviv Rachmiel
  @Test public void c() {
    trimmingOf("int i,j;j++;")//
        .gives("int j;j++;")//
        .gives("int j;++j;") //
        .stays();
  }

  @Test public void t08() {
    trimmingOf("if(b){int i;int j;}else{g();int tipper;}")//
        .gives("if(!b){g();int tipper;}")//
        .gives("if(b)return;g();int tipper;")//
        .gives("if(b)return;g();")//
        .gives("if(b);else g();")//
        .gives("if(!b)g();").stays() //
    ;
  }

  @Test public void t09() {
    trimmingOf("if(b){int i;int j;g();}else{int q;int tipper;}")//
        .gives("if(!b){int q;int tipper;}else{int i;int j;g();}")//
        .gives("if(b){int i;int j;g();}")//
        .gives("if(!b)return;int i;int j;g();")//
        .gives("if(!b)return;g();")//
        .gives("if(!b);else g();")//
        .gives("if(b)g();").stays() //
    ;
  }

  @Ignore // TODO: Raviv Rachmiel
  @Test public void issue075h() {
    trimmingOf("int i = +0;")//
        .gives("");
  }
}
