package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** This is a unit test for {@link ReturnStatementRedundantInVoidMethod} of
 * previously failed tests. Related to {@link Issue0879}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@SuppressWarnings("static-method")
public class Issue0902 {
  @Test public void a() {
    trimminKof("void f(){int x; int y;return;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("void f(){int x; return;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void c() {
    trimminKof("void f(){return;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void d() {
    trimminKof("void f(){int a;}")//
        .gives("void f(){}")//
        .stays();
  }
}
