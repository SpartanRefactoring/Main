package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.wringing.*;

/** 
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class Issue130Test {
  @Test public void issue130_1() {
    trimmingOf("while(true){doSomething();if(done())break;}return something();").gives("while(true){doSomething();if(done())return something();}");
  }

  @Test public void issue130_2() {
    trimmingOf("while(false){doSomething();if(done())break;}return something();").stays();
  }

  @Test public void issue130_3() {
    trimmingOf("while(true){doSomething();if(done()){t+=2;break;}}return something();")
        .gives("while(true){doSomething();if(done()){t+=2;return something();}}");
  }

  @Test public void issue130_4() {
    trimmingOf("for(int i=4 ; true ; ++i){doSomething();if(done())break;}return something();")
        .gives("for(int i=4 ; true ; ++i){doSomething();if(done())return something();}");
  }

  @Test public void issue130_5() {
    trimmingOf("for(int i=4 ; i<s.length() ; ++i){doSomething();if(done())break;}return something();").stays();
  }

  @Test public void issue130_6() {
    trimmingOf("for(int i=4 ; true ; ++i){doSomething();if(done()){t+=2;break;}}return something();")
        .gives("for(int i=4 ; true ; ++i){doSomething();if(done()){t+=2;return something();}}");
  }
}
