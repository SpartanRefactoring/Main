package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0443 {
  @Test public void a() {
    trimmingOf("public void f (String[] ss) {}")//
        .gives("public void f (String[] __) {}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("public void f (String... ss) {}")//
        .gives("public void f (String... __) {}")//
        .stays();
  }
}
