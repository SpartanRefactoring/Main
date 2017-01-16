package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link SwitchWithOneCaseToIf}
 * @author Yuval Simon
 * @since 2016-12-18 */
@SuppressWarnings("static-method")
public class Issue0916 {
  @Test public void c() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4;}")//
        .gives("{if(x==1) {x=2; y=3;} else {x=3; y=4;}}");
  }

  @Test public void d() {
    trimmingOf("switch(x){ default: x=2; y=3; break; case 1: x=3; y=4;}")//
        .gives("{if(x==1) {x=3; y=4;} else {x=2; y=3;}}");
  }

  @Test public void e() {
    trimmingOf("switch(x){ case 1: x=2; y=3; default: x=3;}")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4; break;}")//
        .gives("{if(x==1) {x=2; y=3;} else {x=3; y=4;}}");
  }

  @Test public void h() {
    trimmingOf("switch(x){ case 1: x=2; y=3; default: x=3; break;}")//
        .stays();
  }

  @Test public void t1() {
    trimmingOf("switch(x){ case 1: x=2; y=3; return 5; default: x=3; y=4; break;}")//
        .gives("{if(x==1) {x=2; y=3; return 5;} else {x=3; y=4;}}");
  }

  @Test public void t2() {
    trimmingOf("switch(x){ default: x=2; y=3; return 5; case 1: x=3; y=4; break;}")//
        .gives("{if(x==1) {x=3; y=4;} else {x=2; y=3; return 5;}}");
  }

  @Test public void t3() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4; return 5;}")//
        .gives("{if(x==1) {x=2; y=3;} else {x=3; y=4;return 5;}}");
  }

  @Test public void t4() {
    trimmingOf("switch(x){ case 1: x=2; y=3; return 4; default: x=3; y=4; return 5;}")
        .gives("{if(x==1) {x=2; y=3; return 4;} else {x=3; y=4;return 5;}}");
  }

  @Test public void t5() {
    trimmingOf("while(b) switch(x){ case 1: x=2; y=3; return 4; default: x=3; y=4; return 5;}")
        .gives("while(b) {if(x==1) {x=2; y=3; return 4;} else {x=3; y=4;return 5;}}");
  }

  @Test public void t6() {
    trimmingOf("switch(x){ case 1: case 2: case 3: case 4: x=2; y=3; return 4; default: x=3; y=4; return 5;}")
        .gives("{if(x==1 || x==2 || x==3 || x==4) {x=2; y=3; return 4;} else {x=3; y=4;return 5;}}");
  }

  @Test public void t7() {
    trimmingOf("switch(x){ default: x=2; y=3; return 4; case 1:case 2: case 3: case 4: x=3; y=4; return 5;}")
        .gives("{if(x==1 || x==2 || x==3 || x==4) {x=3; y=4; return 5;} else {x=2; y=3;return 4;}}");
  }
  
  @Test public void t8() {
    trimmingOf("switch(x++){ default: y=2;break; case 1: y=3;}")
        .gives("{if(x++ == 1) { y=3;} else {y=2;}}");
  }
  
  @Test public void t9() {
    trimmingOf("switch(x++){ case 1: y=2;break; default: y=3;}")
        .gives("{if(x++ == 1) { y=2;} else {y=3;}}");
  }
  
  @Test public void t10() {
    trimmingOf("switch(x++){case 1: case 2: y=3; break; default: y=2;break; }").stays();
  }
  
  @Test public void t11() {
    trimmingOf("switch(x++){ case 1: y=2;break; default: case 2: y=3;}")
        .gives("{if(x++ == 1) { y=2;} else {y=3;}}");
  }
}
