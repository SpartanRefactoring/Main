package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** A test class for {@link ParenthesizedRemoveExtraParenthesis}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
@SuppressWarnings("static-method")
public class ParenthesizedRemoveExtraParenthesisTest {
  @Test public void a() {
    trimminKof("((a + b))")//
        .gives("(a+b)")//
        .stays();
  }

  @Test public void b() {
    trimminKof("((T)b)")//
        .stays();
  }

  @Test public void c() {
    trimminKof("a((b))")//
        .gives("a(b)")//
        .stays();
  }

  @Test public void d() {
    trimminKof("((B)b).f()")//
        .stays();
  }

  @Test public void e() {
    trimminKof("((B)b).f")//
        .stays();
  }

  @Test public void f() {
    trimminKof("(a).b();")//
        .gives("a.b();")//
        .stays();
  }

  @Test public void g() {
    trimminKof("(a.b).c();")//
        .gives("a.b.c();")//
        .stays();
  }

  @Test public void h() {
    trimminKof("(a.b()).c();")//
        .gives("a.b().c();")//
        .stays();
  }

  @Test public void i() {
    trimminKof("(a ? b : c).d()")//
        .stays();
  }

  @Test public void j() {
    trimminKof("(a.b(x ? y : z)).d()")//
        .gives("a.b(x ? y : z).d()")//
        .stays();
  }
}
