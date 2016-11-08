package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue359 {
  @Test public void t08() {
    trimmingOf("if(b){int i;int j;}else{g();int tipper;}")//
        .gives("if(!b){g();int tipper;}")//
        .gives("if(b)return;g();int tipper;")//
        .stays() //
    ;
  }
  @Test public void t09() {
    trimmingOf("if(b){int i;int j;g();}else{int q;int tipper;}")//
        .gives("if(!b){int q;int tipper;}else{int i;int j;g();}")//
        .gives("if(b){int i;int j;g();}")//
        .gives("if(!b)return;int i;int j;g();")//
        .stays() //
    ;
  }
  @Test public void t20() {
    trimmingOf("for(;b==q;){int i;}")//
        .gives("{}")//
        .gives("")//
        .stays();
  }
}
