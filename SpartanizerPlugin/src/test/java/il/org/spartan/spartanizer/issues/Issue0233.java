package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** This is a unit test for {@link SwitchEmpty}
 * @author Yuval Simon
 * @since 2016-11-20 */
@SuppressWarnings("static-method")
public class Issue0233 {
  @Test public void a() {
    trimminKof("switch(x) {} int x=5; f(++x);")//
        .gives("int x=5; f(++x);")//
        .stays();
  }

  @Test public void b() {
    trimminKof("switch(x) {} switch(x) {}int x=5; f(++x);")//
        .gives("int x=5; f(++x);")//
        .stays();
  }

  @Test public void c() {
    trimminKof("switch(x) { default: k=5; }")//
        .gives("{k=5;}");
  }

  @Test public void d() {
    trimminKof("switch(x) { default: k=5; break; }")//
        .gives("{k=5;}");
  }

  @Test public void e() {
    trimminKof("switch(x) {} switch(x) { case a: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f() {
    trimminKof("switch(x) { case a: case b: case c: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f2() {
    trimminKof("switch(x) { case a: case b: case c: break;}int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f3() {
    trimminKof("switch(x) { case a: case b: default: case c: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void g() {
    trimminKof("switch(++x) {default: y=3;}")//
        .gives("{++x; y=3;}");
  }

  @Test public void h() {
    trimminKof("switch(++x) {}")//
        .gives("++x;");
  }

  @Test public void i() {
    trimminKof("switch(s.f()) {}")//
        .gives("s.f();");
  }

  @Test public void j() {
    trimminKof("switch(s.f()) {default: y=5;}")//
        .gives("{s.f(); y=5;}");
  }

  @Test public void k() {
    trimminKof("switch(x) {case a: y=5;}")//
        .stays();
  }
}
