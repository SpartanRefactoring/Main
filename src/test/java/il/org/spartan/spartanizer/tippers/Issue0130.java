package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue0130 {
  @Test public void A$01() {
    trimmingOf("while(true){doSomething();if(done())break;}return something();")//
        .gives("while(true){doSomething();if(done())return something();}")//
        .stays();
  }

  @Test public void A$02() {
    trimmingOf("while(false){doSomething();if(done())break;}return something();")//
        .stays();
  }

  @Test public void A$03() {
    trimmingOf("while(true){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("while(true){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }

  @Test public void A$04() {
    trimmingOf("for(int i=4 ; true ; ++i){doSomething(i);if(done())break;}return something();")
        .gives("for(int i=4 ; true ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int i=4 ; ; ++i){doSomething(i);if(done())return something();}")//
        .gives("for(int ¢=4 ; ; ++¢){doSomething(¢);if(done())return something();}")//
        .stays();
  }

  @Test public void A$05() {
    trimmingOf("for(int ¢=4 ; ¢<s.length() ; ++¢){doSomething();if(done())break;}return something();")//
        .stays();
  }

  @Test public void A$06() {
    trimmingOf("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;break;}}return something();")
        .gives("for(int ¢=4 ; true ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")
        .gives("for(int ¢=4 ; ; ++¢){doSomething();if(done()){tipper+=2;return something();}}")//
        .stays();
  }
}
