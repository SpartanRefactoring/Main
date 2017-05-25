package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link RemoveRedundantSwitchCases}
 * @author Yuval Simon
 * @since 2016-11-27 */
@SuppressWarnings("static-method")
public class Issue0880 {
  @Test public void d() {
    trimmingOf("switch(x) {case b:break;case a: default:y=3;break;}")//
        .gives("switch(x){ case b:break; default:y=3;break;}");
  }
  @Test public void e() {
    trimmingOf("switch(x) { case a: case b:x=3;break; default:case c:}")//
        .gives("switch(x) { case a: case b:x=3;break;default:}")//
        .stays();
  }
  @Test public void f() {
    trimmingOf("switch(x) { case c:break;case a: case b:x=3;break;case d: default: x=4;}")
        .gives("switch(x) { case c:break;case a: case b:x=3;break;default:x=4;}");
  }
  @Test public void g() {
    trimmingOf("switch(x) {case a: x=3; case b: break;}")//
        .stays();
  }
  @Test public void i() {
    trimmingOf("switch(x) {case a: x=3; break;case b: y=4; case c: break; case d: y=7; case e: break;  default: break;}")//
        .stays();
  }
  @Test public void j() {
    trimmingOf("switch(x) {case a: case b: x=3;}")//
        .stays();
  }
  @Test public void k() {
    trimmingOf("switch(x){ case a: x=1; break; case b: switch(y) { case c: y=1; break; case d: x=1; break;} break; }")//
        .stays();
  }
}
