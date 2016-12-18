package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link SwitchWithOneCaseToIf}
 * @author Yuval Simon
 * @since 2016-12-18 */
@Ignore
@SuppressWarnings("static-method")
public class Issue916 {
  @Test public void a() {
    trimmingOf("switch(x){ case 1: x=2; }").gives("if(x==1) { x=2; }");
  }
  
  @Test public void b() {
    trimmingOf("switch(x){ case 1: x=2; y=3; break; default: x=3; y=4;}").gives("if(x==1) { x=2; y=3; } else { x=3; y=4;}");
  }
  
  @Test public void c() {
    trimmingOf("switch(x){ case 1: x=2; y=3; default: x=3;}").stays();
  }
}
