package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link RedundentReturnStatementInVoidTypeMethod} of
 * previously failed tests. Related to {@link Issue879}.
 * @author Yuval Simon
 * @since 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue902 {
  @Test public void a() {
    trimmingOf("void f(){int x; int y;return;}")//
        .gives("void f(){int x; int y;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("void f(){int x; return;}")//
        .gives("void f(){int x; }")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("void f(){return;}")//
        .gives("void f(){}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void f(){int a;}")//
        .gives("void f(){}")//
        .stays();
  }
}
