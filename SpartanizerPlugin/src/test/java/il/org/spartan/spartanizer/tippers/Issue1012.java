package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** Unit tests for {@link TwoDeclarationsIntoOne}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-13 */
@SuppressWarnings("static-method")
public class Issue1012 {
  @Test public void a() {
    trimmingOf("int a = 0; int b = 1; int c = 2; f(); g();")//
        .gives("int a = 0, b = 1; int c = 2; f(); g();").gives("int a = 0, b = 1, c = 2; f(); g();");
  }

  @Test public void b() {
    trimmingOf("int a; int b; f();")//
        .gives("int a, b; f();");
  }

  @Test public void c() {
    trimmingOf("int a; int b = 1; int c; f(); g();")//
        .gives("int a, b = 1; int c; f(); g();")//
        .gives("int a, b = 1, c; f(); g();");
  }
}
