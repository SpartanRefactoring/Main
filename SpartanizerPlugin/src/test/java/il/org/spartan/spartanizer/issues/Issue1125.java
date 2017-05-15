package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** See GitHub issue with thus numbered unit test for
 * {@link ForEmptyBlockToEmptyStatement},
 * {@link WhileEmptyBlockToEmptyStatement} and
 * {@link DoWhileEmptyBlockToEmptyStatement}
 * @author Yossi Gil
 * @author Niv Shalmon
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue1125 {
  @Test public void forLoop() {
    trimmingOf("for(x();y();z()){}")//
        .gives("for(x();y();z());")//
        .stays()//
    ;
  }
  @Test public void forLoop2() {
    trimmingOf("for(x();y();z()){f();g();}")//
        .stays()//
    ;
  }
  @Test public void whileLoop() {
    trimmingOf("while(x()){}")//
        .gives("while(x());")//
        .stays()//
    ;
  }
  @Test public void whileLoop2() {
    trimmingOf("while(x()){f();g();}")//
        .gives("for(;x(); g()){f();}")//
        .gives("for(;x(); g())f();")//
        .stays()//
    ;
  }
  @Test public void doLoop() {
    trimmingOf("do{}while(x());")//
        .gives("do;while(x());")//
        .stays()//
    ;
  }
  @Test public void doLoop2() {
    trimmingOf("do{f();g();}while(x());")//
        .stays()//
    ;
  }
}
