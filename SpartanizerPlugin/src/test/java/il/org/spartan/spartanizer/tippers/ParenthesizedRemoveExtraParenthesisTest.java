package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** A test class for {@link ParenthesizedRemoveExtraParenthesis}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
@SuppressWarnings("static-method")
public class ParenthesizedRemoveExtraParenthesisTest {
  @Test public void a() {
    topDownTrimming("((a + b))")//
        .gives("(a+b)")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("((T)b)")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("a((b))")//
        .gives("a(b)")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("((B)b).f()")//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("((B)b).f")//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("(a).b();")//
        .gives("a.b();")//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("(a.b).c();")//
        .gives("a.b.c();")//
        .stays();
  }

  @Test public void h() {
    topDownTrimming("(a.b()).c();")//
        .gives("a.b().c();")//
        .stays();
  }

  @Test public void i() {
    topDownTrimming("(a ? b : c).d()")//
        .stays();
  }

  @Test public void j() {
    topDownTrimming("(a.b(x ? y : z)).d()")//
        .gives("a.b(x ? y : z).d()")//
        .stays();
  }
}
