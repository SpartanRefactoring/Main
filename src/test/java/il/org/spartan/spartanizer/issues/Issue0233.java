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
    topDownTrimming("switch(x) {} int x=5; f(++x);")//
        .gives("int x=5; f(++x);")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("switch(x) {} switch(x) {}int x=5; f(++x);")//
        .gives("int x=5; f(++x);")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("switch(x) { default: k=5; }")//
        .gives("{k=5;}");
  }

  @Test public void d() {
    topDownTrimming("switch(x) { default: k=5; break; }")//
        .gives("{k=5;}");
  }

  @Test public void e() {
    topDownTrimming("switch(x) {} switch(x) { case a: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f() {
    topDownTrimming("switch(x) { case a: case b: case c: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f2() {
    topDownTrimming("switch(x) { case a: case b: case c: break;}int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void f3() {
    topDownTrimming("switch(x) { case a: case b: default: case c: }int x=5; f(++x);")//
        .gives("int x=5; f(++x);");
  }

  @Test public void g() {
    topDownTrimming("switch(++x) {default: y=3;}")//
        .gives("{++x; y=3;}");
  }

  @Test public void h() {
    topDownTrimming("switch(++x) {}")//
        .gives("++x;");
  }

  @Test public void i() {
    topDownTrimming("switch(s.f()) {}")//
        .gives("s.f();");
  }

  @Test public void j() {
    topDownTrimming("switch(s.f()) {default: y=5;}")//
        .gives("{s.f(); y=5;}");
  }

  @Test public void k() {
    topDownTrimming("switch(x) {case a: y=5;}")//
        .stays();
  }
}
