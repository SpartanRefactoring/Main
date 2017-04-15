package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link MergeSwitchBranches}
 * @author Yuval Simon
 * @since 2016-11-26 */
@SuppressWarnings("static-method")
public class Issue0858 {
  @Test public void a() {
    trimminKof("switch(x){case a: x=1; break; case b: x=2; break; default: x=1; break; }")
        .gives("switch(x){ case a: default: x=1; break; case b: x=2; break; }");
  }

  @Test public void b() {
    trimminKof("switch(x){ case a: x=1; break; case b: x=1; break; }")//
        .gives("switch(x){ case a: case b: x=1; break; }");
  }

  @Test public void c() {
    trimminKof("switch(x){case a: x=1; break; case b: x=2; break; default: x=1;}")
        .gives("switch(x){ case a: default: x=1; break; case b: x=2; break; }");
  }

  @Test public void d() {
    trimminKof("switch(x){ case a: case b: case c: x=1; break; case d: x=1; break; case e: x=2; break; }")
        .gives("switch(x){ case a: case b: case c: case d: x=1; break; case e: x=2; break; }");
  }

  @Test public void e() {
    trimminKof("switch(x){ case a: case b: case c: x=1; x=1; break; case d: x=1; x=1; break; case e: x=2; break; }")
        .gives("switch(x){ case a: case b: case c: case d: x=1; x=1; break; case e: x=2; break; }");
  }

  @Test public void f() {
    trimminKof("switch(x){case e:x=2;break;case a:case b:case c:x=1;case d:x=1;break;}")//
        .stays();
  }

  @Test public void g() {
    trimminKof("switch(x){ case a: case b: case c: x=1; break; case e: x=2; break; case d: x=1; }")
        .gives("switch(x){ case a: case b: case c: case d: x=1; break; case e: x=2; break; }");
  }

  @Test public void h() {
    trimminKof("switch(x){case d:x=1;break;case e:x=2;break;case a:case b:case c:x=1;x=1;break;}")//
        .stays();
  }

  @Test public void i() {
    trimminKof("switch(x){ case a: x=1; break; case b: switch(y) { case c: y=1; break; case d: x=1; break;} break; }")//
        .stays();
  }

  @Test public void j() {
    trimminKof("switch(x){case a:switch(y){case a:y=1;}break;case d:switch(y){case b:y=1;}break;"
        + "case b:x=2;switch(y){case a:y=1;}break;case c:z=3;x=2;switch(y){case a:y=1;}break;}")//
            .stays();
  }

  @Test public void k() {
    trimminKof("switch(x){ case a: switch(y) {case a: y=1;} break; case b: x=2;"
        + "switch(y) {case a: y=1;} break; case c: z=3; x=2; switch(y) { case a: y=1;} break; case d: switch(y) {case a: y=1;}}")
            .gives("switch(x){ case a: case d: switch(y) {case a: y=1;} break; case b: x=2;"
                + "switch(y) {case a: y=1;} break; case c: z=3; x=2; switch(y) { case a: y=1;} break;}");
  }

  @Test public void l() {
    trimminKof("switch(x){case b:y=2;z=3;break;case a:x=1;y=2;z=3;break;case c:x=2;y=2;z=3;break;}")//
        .stays();
  }

  @Test public void m() {
    trimminKof("switch(x){ case a: x=1; case b: x=2; break; case c: x=1; x=2; break; case d: case e: x=2; break; case f: case g: x=2;}")
        .gives("switch(x){ case a: x=1; case b: x=2; break; case c: x=1; x=2; break; case d: case e: case f: case g: x=2; break;}");
  }
}
