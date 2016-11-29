package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Yuval Simon
 * @since 2016-11-27 */
@SuppressWarnings("static-method")
@Ignore
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
}
