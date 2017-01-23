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
    trimmingOf("int a; int b; f(a, b);")//
        .gives("int a, b; f(a, b);");
  }

  @Test public void c() {
    trimmingOf("int a; int b = 1; int c; f(a,b); g(d,c);")//
        .gives("int a, b = 1; int c; f(a,b); g(d,c);")//
        .gives("int a, b = 1, c; f(a,b); g(d,c);");
  }

  @Test public void d() {
    trimmingOf("@Nullable final String[] parts = { null }; @NotNull final String[] t = CSV.split(CSV.combine(parts)); f(parts); g();").stays();
  }

  @Test public void e() {
    trimmingOf("@NotNull final int a[] = Permutation.random(10000); int count = 0;").stays();
  }

  @Test public void f() {
    trimmingOf("final int a = 0; int b = 8; f(); g();").stays();
  }

  @Test public void g() {
    trimmingOf("@NotNull final int a = 0; @NotNull final int b = 8; f(); g();").gives("@NotNull final int a = 0, b = 8; f(); g();");
  }
}
