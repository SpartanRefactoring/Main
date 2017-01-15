package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link RemoveRedundantSwitchCases}
 * @author Yuval Simon
 * @since 2016-11-27 */
@Ignore // TODO Yuval Simon
@SuppressWarnings("static-method")
public class Issue0880 {
  @Test public void c() {
    trimmingOf("switch(x) { case a: default:y=3; case b:}")//
        .gives("switch(x){default:y=3; case b:}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("switch(x) { case a: default:y=3;break; case b:break;}")//
        .gives("switch(x){default:y=3;break; case b:break;}")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("switch(x) { case a: case b:x=3;break; default:case c:}")//
        .gives("switch(x) { case a: case b:x=3;break;default:}")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("switch(x) { case a: case b:x=3;break;case d: default: x=4; case c:}")
        .gives("switch(x) { case a: case b:x=3;break;default:x=4; case c:}");
  }

  @Test public void g() {
    trimmingOf("switch(x) {case a: x=3; case b: break;}")//
        .stays();
  }

  @Test public void i() {
    trimmingOf("switch(x) {case a: x=3; case b: y=4; case c: break; default: break; case d: y=7; case e:}")//
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
