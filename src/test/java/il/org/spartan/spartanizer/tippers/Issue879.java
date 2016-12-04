package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue879 {
  @Test public void test0() {
    trimmingOf("void f(){return;}").gives("void f(){}").stays();
  }

  @Test public void test1() {
    trimmingOf("void f(){int x; int y;return;}").gives("void f(){int x; int y;}").gives("void f(){}").stays();
  }

  @Test public void test2() {
    trimmingOf("void f(){int x=4; if(x>3){x+=3;x+=4;}else{x+=5;x+=55;} return;}").gives("void f(){int x=4; if(x>3){x+=3;x+=4;}else{x+=5;x+=55;}}")
        .stays();
  }

  @Test public void test3() {
    trimmingOf("void f(){int x=9;int y=7; x+=4;y=x+x;return;}").gives("void f(){int x=9;int y=7; x+=4;y=x+x;}").stays();
  }

  @Test public void test4() {
    trimmingOf("int f(){int x=9;int y=7; x+=4;y=x+x;return;}").stays();
  }
}