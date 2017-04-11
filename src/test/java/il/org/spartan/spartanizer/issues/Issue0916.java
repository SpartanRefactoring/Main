package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link SwitchWithOneCaseToIf}
 * @author Yuval Simon
 * @since 2016-12-18 */
@SuppressWarnings("static-method")
public class Issue0916 {
  @Test public void t1() {
    topDownTrimming("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4;}")//
        .gives("if(x==1) {x=2; y=3;} else {x=3; y=4;}");
  }

  @Test public void t2() {
    topDownTrimming("switch(x){ default: x=2; y=3; break; case 1: x=3; y=4;}")//
        .gives("if(x==1) {x=3; y=4;} else {x=2; y=3;}");
  }

  @Test public void t3() {
    topDownTrimming("switch(x){ case 1: break; default: x=3; y=4;}")//
        .gives("if(!(x==1)) {x=3; y=4;}");
  }

  @Test public void t4() {
    topDownTrimming("switch(x){ case 1: x=2; y=3; break; default:}")//
        .gives("if(x==1) {x=2; y=3;}");
  }

  @Test public void t5() {
    topDownTrimming("y=2; switch(x){ case 1:  break; default:}")//
        .gives("y=2;");
  }
}
