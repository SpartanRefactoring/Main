package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
 //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0443 {
  @Test public void a() {
    trimminKof("public void f (String[] ss) {}")//
        .gives("public void f (String[] __) {}")//
        .stays();
  }

  @Test public void b() {
    trimminKof("public void f (String... ss) {}")//
        .gives("public void f (String... __) {}")//
        .stays();
  }
}
