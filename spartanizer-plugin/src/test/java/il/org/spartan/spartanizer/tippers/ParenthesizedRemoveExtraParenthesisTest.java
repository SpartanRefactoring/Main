package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

/** A test class for {@link ParenthesizedRemoveExtraParenthesis}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
@SuppressWarnings("static-method")
public class ParenthesizedRemoveExtraParenthesisTest {
  @Test public void a() {
    trimmingOf("((a + b))")//
        .gives("(a+b)")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("((T)b)")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("a((b))")//
        .gives("a(b)")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("((B)b).f()")//
        .stays();
  }
  @Test public void e() {
    trimmingOf("((B)b).f")//
        .stays();
  }
  @Test public void f() {
    trimmingOf("(a).b();")//
        .gives("a.b();")//
        .stays();
  }
  @Test public void g() {
    trimmingOf("(a.b).c();")//
        .gives("a.b.c();")//
        .stays();
  }
  @Test public void h() {
    trimmingOf("(a.b()).c();")//
        .gives("a.b().c();")//
        .stays();
  }
  @Test public void i() {
    trimmingOf("(a ? b : c).d()")//
        .stays();
  }
  @Test public void j() {
    trimmingOf("(a.b(x ? y : z)).d()")//
        .gives("a.b(x ? y : z).d()")//
        .stays();
  }
}
