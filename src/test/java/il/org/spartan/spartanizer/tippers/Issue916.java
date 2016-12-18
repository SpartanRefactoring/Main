package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link SwitchWithOneCaseToIf}
 * @author Yuval Simon
 * @since 2016-12-18 */

@SuppressWarnings("static-method")
public class Issue916 {
  @Test public void a() {
    trimmingOf("switch(x){ case 1: x=2; }").gives("{if(x==1) { x=2; }}").gives("if(x==1) { x=2; }");
  }
  
  @Test public void b() {
    trimmingOf("switch(x){ case 1: x=2; y=3; }").gives("{if(x==1) {x=2; y=3;}}");
  }
  
  @Test public void c() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4;}").gives("{if(x==1) {x=2; y=3;} else {x=3; y=4;}}");
  }
  
  @Test public void d() {
    trimmingOf("switch(x){ default: x=2; y=3; break; case 1: x=3; y=4;}").gives("{if(x==1) {x=3; y=4;} else {x=2; y=3;}}");
  }
  
  @Test public void e() {
    trimmingOf("switch(x){ case 1: x=2; y=3; default: x=3;}").stays();
  }
  
  @Test public void f() {
    trimmingOf("switch(x){ case 1: x=2; break; }").gives("{if(x==1) { x=2; }}");
  }
  
  @Test public void g() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4; break;}").gives("{if(x==1) {x=2; y=3;} else {x=3; y=4;}}");
  }
  
  @Test public void h() {
    trimmingOf("switch(x){ case 1: x=2; y=3; default: x=3; break;}").stays();
  }
}
