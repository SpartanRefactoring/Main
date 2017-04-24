package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0130 {
  @Test public void A$01() {
    trimminKof("while(true){doSomething();if(done())break;}return something();")//
        .gives("while(true){doSomething();if(done())return something();}")//
        .stays();
  }

  @Test public void A$02() {
    trimminKof("while(false){doSomething();if(done())break;}return something();")//
        .stays();
  }

  @Test public void A$03() {
    trimminKof("while(true){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("while(true){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }

  @Test public void A$04() {
    trimminKof("for(int i=4 ; true ; ++i){doSomething(i);if(done())break;}return something();")
        .gives("for(int i=4 ; true ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int i=4 ; ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int ¢=4 ; ; ++¢){doSomething(¢);if(done())return something();}")//
        .stays();
  }

  @Test public void A$05() {
    trimminKof("for(int ¢=4 ; ¢<s.length() ; ++¢){doSomething();if(done())break;}return something();")//
    ;
  }

  @Test public void A$06() {
    trimminKof("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")
        .gives("for(int ¢=4 ; ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }
}
