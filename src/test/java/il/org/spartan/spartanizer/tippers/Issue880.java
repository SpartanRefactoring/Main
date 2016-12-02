package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Yuval Simon
 * @since 2016-11-27 */
@SuppressWarnings("static-method")
public class Issue880 {
  @Test public void a() {
    trimmingOf("switch(x) { case a: default: case b:}").gives("switch(x){}");
  }

  @Test public void b() {
    trimmingOf("switch(x) { case x: case y: break; case a: break; default: case b:}").gives("switch(x){}");
  }

  @Test public void c() {
    trimmingOf("switch(x) { case a: default:y=3; case b:}").gives("switch(x){default:y=3;}");
  }

  @Test public void d() {
    trimmingOf("switch(x) { case a: default:y=3;break; case b:break;}int x=5;").gives("switch(x){default:y=3;break;}int x=5;");
  }

  @Test public void e() {
    trimmingOf("switch(x) { case a: case b:x=3;break; default:case c:}").gives("switch(x) { case a: case b:x=3;break;}");
  }

  @Test public void f() {
    trimmingOf("switch(x) { case a: case b:x=3;break;case d: default: x=4; case c:}").gives("switch(x) { case a: case b:x=3;break;default:x=4;}");
  }

  @Test public void g() {
    trimmingOf("switch(x) {case a: x=3; case b: break;}").gives("switch(x) {case a: x=3; break;}");
  }

  @Test public void h() {
    trimmingOf("switch(x) {case a: x=3; case b: case c: break; default: break; case d: case e:}").gives("switch(x) {case a: x=3; break;}");
  }

  @Test public void i() {
    trimmingOf("switch(x) {case a: x=3; case b: y=4; case c: break; default: break; case d: y=7; case e:}")
        .gives("switch(x) {case a: x=3; case b:y=4; break; case d: y=7;}");
  }

  @Test public void j() {
    trimmingOf("switch(x) {case a: case b: x=3;}").stays();
  }
}
