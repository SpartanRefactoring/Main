package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0428 {
  @Test public void a() {
    trimmingOf("func(int i){int j;System.out.println(j);System.out.println($);return j;}")
        .gives("func(int __){int j;System.out.println(j);System.out.println($);return j;}")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("int func() {int j = 0; System.out.print(j); return j;}")//
        .gives("int func() {int $ = 0; System.out.print($); return $;}")//
        .stays();
  }

  @Test public void d1() {
    trimmingOf("int func(int k) {int j = 0; System.out.print(j); if(f()) return j; return k;}")
        .gives("int func(int k) {int $ = 0; System.out.print($); if(f()) return $; return k;}")
        .gives("int func(int k) {int $ = 0; System.out.print($); return f()?$:k;}")//
        .stays();
  }

  @Test public void d2() {
    trimmingOf("int func() {int j = 0; int k; System.out.print(j); if(f()) return j; return k;}")
        .gives("int func() {int $ = 0; int k; System.out.print($); if(f()) return $; return k;}")
        .gives("int func() {int $ = 0; int k; System.out.print($); return f()?$:k;}");
  }

  @Test public void e() {
    trimmingOf("int func() {int j = 0; System.out.print($); return j;}")//
        .stays();
  }
}
