package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0130 {
  @Test public void A$01() {
    topDownTrimming("while(true){doSomething();if(done())break;}return something();")//
        .gives("while(true){doSomething();if(done())return something();}")//
        .stays();
  }

  @Test public void A$02() {
    topDownTrimming("while(false){doSomething();if(done())break;}return something();")//
        .stays();
  }

  @Test public void A$03() {
    topDownTrimming("while(true){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("while(true){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }

  @Test public void A$04() {
    topDownTrimming("for(int i=4 ; true ; ++i){doSomething(i);if(done())break;}return something();")
        .gives("for(int i=4 ; true ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int i=4 ; ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int ¢=4 ; ; ++¢){doSomething(¢);if(done())return something();}")//
        .stays();
  }

  @Test public void A$05() {
    topDownTrimming("for(int ¢=4 ; ¢<s.length() ; ++¢){doSomething();if(done())break;}return something();")//
    ;
  }

  @Test public void A$06() {
    topDownTrimming("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")
        .gives("for(int ¢=4 ; ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }
}
